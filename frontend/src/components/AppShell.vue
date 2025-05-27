<script setup>
import { ref, onUnmounted, nextTick, computed } from 'vue';
import { useUploadStore } from '@/stores/uploadStore';
import SidebarMainLayout from '@/components/layout/SidebarMainLayout.vue';
import UploadGuideModal from '@/components/modal/UploadGuideModal.vue';
import JjureUploadModal from '@/features/jjure/components/JjureUploadModal.vue';
import FeedUploadModal from '@/features/feed/components/FeedUploadModal.vue';
import NotificationModal from '@/features/notification/components/NotificationModal.vue';
import { createFeed, uploadImages } from '@/api/feed.js';
import { showErrorToast, showSuccessToast } from '@/utills/toast.js';
import {startLoading, startLoadingAsync, stopLoadingAsync} from '@/composable/useLoadingBar.js';
import { useFeedRefreshStore } from '@/stores/feedRefreshStore.js';
import { useRouter } from 'vue-router';
import {
  getPresignedUrl,
  saveJjureMeta,
  uploadFileToS3,
  uploadThumbnailImage,
} from '@/api/jjure.js';
import { useAuthStore } from '@/stores/auth.js'


const showUploadGuideModal = ref(false);
const showJjureUploadModal = ref(false);
const showFeedUploadModal = ref(false);
const showNotificationModal = ref(false);
const imageUrls = ref([]);
const imageFiles = ref([]);
const videoUrl = ref('');
const caption = ref('');
const router = useRouter();
const thumbnailBlob = ref(null); // 썸네일 Blob 저장

const uploadStore = useUploadStore();
const feedRefreshStore = useFeedRefreshStore();
const authStore = useAuthStore();

// 파일 선택 핸들러
async function handleFilesSelected({ existingUrls = [], files = [] }) {
  console.log(files);
  if (!files.length) return;

  const file = files[0];

  const isVideo = file.type.startsWith('video/');
  const isAllImages = files.every((f) => f.type.startsWith('image/'));

  if (isVideo && files.length === 1) {
    uploadStore.setFile(file);
    videoUrl.value = URL.createObjectURL(file);

    await nextTick();

    showUploadGuideModal.value = false;
    showJjureUploadModal.value = true;
    return;
  }

  if (isAllImages) {
    imageFiles.value = files;
    imageUrls.value = [...existingUrls, ...files.map((f) => URL.createObjectURL(f))];
    showUploadGuideModal.value = false;
    showFeedUploadModal.value = true;
  }
}

/* 쭈르 동영상 업로드 */
async function handleUpload() {
  const file = uploadStore.selectedFile;
  if (!file || !thumbnailBlob.value) return;

  try {
    startLoadingAsync();

    // 1. 썸네일 S3 업로드
    const thumbnailRes = await uploadThumbnailImage(thumbnailBlob.value);
    const thumbnailUrl = thumbnailRes.data.data;

    // 2. Presigned URL 발급
    const { presignedUrl, fileKey } = await getPresignedUrl(file.name, file.type);

    // 3. S3에 동영상 업로드
    await uploadFileToS3(presignedUrl, file);

    // 4. 메타데이터 저장 API 호출 (썸네일 포함)
    await saveJjureMeta({ fileKey, caption: caption.value, thumbnail_url: thumbnailUrl });

    showSuccessToast('쭈르 업로드에 성공했습니다!');
  } catch (error) {
    showErrorToast('업로드 중 오류가 발생했습니다.');
  } finally {
    showJjureUploadModal.value = false;
    URL.revokeObjectURL(videoUrl.value);
    videoUrl.value = '';
    caption.value = '';
    uploadStore.setFile(null);
    thumbnailBlob.value = null;
    stopLoadingAsync();
  }
}

async function handleFeedUpload() {
  try {
    const formData = new FormData();
    startLoading();
    imageFiles.value.forEach((file) => formData.append('files', file));

    const res = await uploadImages(formData);
    const uploadedImageUrls = res.data.data;

    const payload = {
      content: caption.value,
      imageUrls: uploadedImageUrls,
    };

    await createFeed(payload);

    showSuccessToast('피드 업로드에 성공했습니다!');

    feedRefreshStore.triggerRefresh();
    showFeedUploadModal.value = false;
    await router.push('/feed');

    imageFiles.value = [];
    imageUrls.value.forEach((url) => URL.revokeObjectURL(url));
    imageUrls.value = [];
    caption.value = '';
  } catch (err) {
    console.error('피드 업로드 실패:', err);
    const errorCode = err.response?.data?.errorCode;
    console.log('errorCode=', errorCode);
    if (errorCode === '04004') {
      showErrorToast('강아지 이미지가 발견되었습니다. 고양이만 등록해주세요~^^😺😺😺');
    } else if (errorCode === '04005') {
      showErrorToast('고양이가 없는 이미지가 발견되었습니다. 고양이를 등록해주세요~^^😺😺😺');
    } else {
      showErrorToast('피드 업로드중 에러가 발생했습니다!');
    }
  }
}

const handleUpdateThumbnail = (blob) => {
  console.log('썸네일 전달 받음:', blob);
  thumbnailBlob.value = blob;
};


function handleShowNotificationModal() {
  if(!authStore.isAuthenticated){
    showErrorToast('로그인이 필요합니다.')
    return;
  }
  showNotificationModal.value = true;
}

// 메모리 정리
onUnmounted(() => {
  if (videoUrl.value) {
    URL.revokeObjectURL(videoUrl.value);
  }
});
</script>

<template>
  <div>
    <!-- 사이드바 포함 전체 레이아웃 -->
    <SidebarMainLayout
      @open-upload-modal="showUploadGuideModal = true"
      @open-notification-modal="handleShowNotificationModal"
    >
      <RouterView />
    </SidebarMainLayout>

    <NotificationModal
      v-show="showNotificationModal"
      :is-modal-open="showNotificationModal"
      @close="showNotificationModal = false"
    />

    <!-- 파일 업로드 안내 모달 -->
    <UploadGuideModal
      v-if="showUploadGuideModal"
      @close="showUploadGuideModal = false"
      @fileSelected="handleFilesSelected"
    />

    <FeedUploadModal
      v-if="showFeedUploadModal"
      @close="showFeedUploadModal = false"
      v-model:caption="caption"
      @upload="handleFeedUpload"
      :imageUrls="imageUrls"
    />

    <JjureUploadModal
      v-if="showJjureUploadModal"
      :videoUrl="videoUrl"
      v-model:caption="caption"
      @update:thumbnail="handleUpdateThumbnail"
      @close="showJjureUploadModal = false"
      @upload="handleUpload"
    />
  </div>
</template>
