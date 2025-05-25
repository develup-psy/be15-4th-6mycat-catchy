<script setup>
import { computed, ref, watch } from 'vue';
import { RouterLink } from 'vue-router';
import { likeFeed, unLikeFeed } from '@/api/like.js';
import {
  deleteJjure,
  getPresignedUrl,
  uploadFileToS3,
  uploadThumbnailImage,
  updateJjure,
} from '@/api/jjure.js';
import EditDeleteDropdown from '@/components/EditDeleteDropdown.vue';
import JjureUploadModal from '@/features/jjure/components/JjureUploadModal.vue';
import DeleteModal from '@/components/modal/DeleteModal.vue';
import { showErrorToast, showSuccessToast } from '@/utills/toast.js';
import {
  startLoading,
  startLoadingAsync,
  stopLoading,
  stopLoadingAsync,
} from '@/composable/useLoadingBar.js';
import ShareDropdown from '@/components/ShareDropdown.vue';

const props = defineProps({
  id: Number,
  fileKey: String,
  authorNickname: String,
  authorImageUrl: String,
  caption: String,
  musicTitle: String,
  commentPreview: String,
  likeCount: Number,
  commentCount: Number,
  isLiked: Boolean,
  isMine: Boolean,
  thumbnailUrl: String,
});

const file = ref(null);

const videoUrl = ref(
  `https://${import.meta.env.VITE_AWS_BUCKET_NAME}.s3.${import.meta.env.VITE_AWS_REGION}.amazonaws.com/${props.fileKey}`,
);

const caption = ref(props.caption);
const thumbnailBlob = ref(null);
const showEditModal = ref(false);
const showDeleteModal = ref(false);
const animateLike = ref(false);

const likeRequest = computed(() => ({ targetId: props.id, targetType: 'JJURE' }));
const likeCount = ref(props.likeCount);
const isLiked = ref(props.isLiked);

const handleLikeClick = async () => {
  try {
    if (isLiked.value) {
      await unLikeFeed(likeRequest.value);
      likeCount.value -= 1;
    } else {
      await likeFeed(likeRequest.value);
      likeCount.value += 1;
    }
    isLiked.value = !isLiked.value;
    animateLike.value = true;
    setTimeout(() => (animateLike.value = false), 200);
  } catch (e) {
    console.error('좋아요 처리 실패:', e);
  }
};

const handleEdit = () => (showEditModal.value = true);
const handleDelete = () => (showDeleteModal.value = true);
const closeEditModal = () => (showEditModal.value = false);
const closeDeleteModal = () => (showDeleteModal.value = false);

const deleteJjureHandler = async () => {
  startLoading();
  try {
    await deleteJjure(props.id);
    showSuccessToast('쭈르 삭제에 성공했습니다!');
    location.reload();
  } catch (e) {
    stopLoading();
    console.error('삭제 실패:', e);
    showErrorToast('삭제에 실패했습니다!!');
  }
  closeDeleteModal();
};

const onVideoSelected = (e) => {
  const selected = e.target.files[0];
  if (!selected) return;
  file.value = selected;
  videoUrl.value = URL.createObjectURL(selected);
};

const updateJjureHandler = async () => {
  startLoadingAsync();

  try {
    let newFileKey = props.fileKey;
    let newThumbnailUrl = null;

    // 썸네일 변경 시 S3 업로드
    if (thumbnailBlob.value) {
      const thumbnailRes = await uploadThumbnailImage(thumbnailBlob.value);
      newThumbnailUrl = thumbnailRes.data.data;
    }

    // 동영상 변경 시 Presigned URL 발급 + 업로드
    if (file.value) {
      const { presignedUrl, fileKey } = await getPresignedUrl(file.value.name, file.value.type);
      await uploadFileToS3(presignedUrl, file.value);
      newFileKey = fileKey;
    }

    // 업데이트 API 호출
    await updateJjure(props.id, {
      id: props.id,
      fileKey: newFileKey,
      caption: caption.value,
      thumbnailUrl: newThumbnailUrl,
    });

    showSuccessToast('쭈르 수정이 완료되었습니다!');
    closeEditModal();
    location.reload();
  } catch (err) {
    console.error('수정 실패:', err);
    showErrorToast('쭈르 수정에 실패했습니다.');
  } finally {
    stopLoadingAsync();
  }
};
</script>

<template>
  <div class="jjure-container">
    <div class="jjure-card">
      <div class="video-wrapper">
        <video autoplay muted loop playsinline>
          <source :src="videoUrl" type="video/mp4" />
          브라우저가 비디오를 지원하지 않습니다.
        </video>

        <div v-if="isMine" class="absolute top-3 right-3 z-20 shadow-elevated px-3 py-1 rounded-md">
          <EditDeleteDropdown @edit="handleEdit" @delete="handleDelete" />
        </div>

        <div class="reel-actions">
          <button class="icon-wrapper" @click="handleLikeClick">
            <i
              :class="[
                isLiked ? 'fa-solid' : 'fa-regular',
                'fa-heart text-primary cursor-pointer transition-transform duration-200',
                animateLike ? 'scale-150' : '',
              ]"
            ></i>
            <div class="action-count">{{ likeCount }}</div>
          </button>
          <RouterLink :to="`/jjure/${id}`" class="icon-wrapper">
            <i class="fa-regular fa-comment text-primary"></i>
            <div class="action-count">{{ commentCount }}</div>
          </RouterLink>
          <div>
            <ShareDropdown
              :shareUrl="`http://localhost:5173/jjure/${id}`"
              :shareText="caption"
              :shareImage="thumbnailUrl"
            />
          </div>
        </div>
      </div>

      <div class="caption-box">
        <div class="title">
          <RouterLink :to="`/members/${authorNickname}`" class="nickname hover:underline">
            @{{ authorNickname }}
          </RouterLink>
          <span class="caption">{{ caption }}</span>
        </div>
        <div v-if="commentPreview" class="comment-preview">{{ commentPreview }}</div>
        <div v-if="musicTitle" class="music-info">
          <i class="fa-solid fa-music"></i> {{ musicTitle }}
        </div>
      </div>

      <div class="footer">ⓒ 2025 Catchy</div>

      <JjureUploadModal
        v-if="showEditModal"
        v-model:caption="caption"
        v-model:videoUrl="videoUrl"
        v-model:thumbnail="thumbnailBlob"
        @upload="updateJjureHandler"
        @close="closeEditModal"
      >
        <template #video-upload-slot>
          <label
            class="inline-block px-4 py-2 bg-white border-primary border-2 text-primary text-body-sm rounded-md cursor-pointer text-center mt-4"
          >
            <span>동영상 수정</span>
            <input type="file" accept="video/*" @change="onVideoSelected" hidden />
          </label>
        </template>
      </JjureUploadModal>

      <DeleteModal
        v-if="showDeleteModal"
        @close="closeDeleteModal"
        @confirm="closeDeleteModal"
        @delete="deleteJjureHandler"
      />
    </div>
  </div>
</template>

<style scoped>
.jjure-container {
  @apply flex justify-center;
}
.jjure-card {
  @apply w-[400px] bg-white rounded-xl overflow-hidden shadow-hover text-black flex flex-col;
}
.video-wrapper {
  @apply relative w-full bg-black flex-shrink-0 h-[640px];
  aspect-ratio: 9 / 16;
}
.video-wrapper video {
  @apply w-full h-full object-cover;
  aspect-ratio: 9 / 16;
}
.reel-actions {
  @apply absolute right-2 bottom-14 flex flex-col gap-4 text-white text-[16px] items-center;
}
.icon-wrapper {
  @apply cursor-pointer text-center shadow-elevated bg-black-alpha-60 rounded-full p-2;
}
.reel-actions i {
  @apply hover:text-primary;
}
.action-count {
  @apply text-[12px] mt-1 text-center;
}
.caption-box {
  @apply flex-1  bg-white p-3 text-[13px] border-t border-gray-200 text-gray-800;
}
.caption {
  @apply inline-block truncate text-[13px] max-w-[300px] text-gray-700;
}
.title {
  @apply flex items-center;
}
.title .nickname {
  @apply font-bold text-primary mr-2;
}
.comment-preview {
  @apply mt-1 text-[12px] text-gray-900;
}
.music-info {
  @apply mt-2 text-[12px] text-gray-700 flex items-center gap-1;
}
.footer {
  @apply bg-white text-gray-400 text-[11px] border-t border-gray-200 h-[36px] flex flex-col justify-center items-center leading-none flex-grow-0;
}
</style>
