<template>
  <div class="fixed inset-0 bg-black/50 flex justify-center items-center z-50">
    <div class="bg-white w-[75%] max-w-5xl h-[65%] rounded-xl flex overflow-hidden relative">
      <!-- 좌측: 이미지 -->
      <div class="w-2/3 bg-black relative flex items-center justify-center">
        <FeedCarousel v-if="feed" :images="feed.imageUrls" />

        <!-- 오버레이 버튼 -->
        <div
          class="absolute right-4 bottom-4 flex flex-col items-center gap-4 text-white text-sm p-2"
        >
          <button class="flex flex-col items-center opacity-80 hover:opacity-100">
            <i
              :class="[
                liked ? 'fa-solid' : 'fa-regular',
                'fa-heart cursor-pointer transition-transform duration-200',
                animateLike ? 'scale-150' : '',
              ]"
              @click="toggleLike"
            />
            <span>{{ likeCount }}</span>
          </button>
          <button class="flex flex-col items-center opacity-80 hover:opacity-100">
            <i class="fa-regular fa-comment"></i>
            <span>{{ feed?.commentCount }}</span>
          </button>
          <ShareDropdown
            :shareUrl="`https://catchy.site/feed`"
            :shareText="caption"
            :shareImage="feed?.imageUrls[0]"
          />
        </div>
      </div>

      <!-- 우측: 댓글/작성자 -->
      <div class="w-1/3 flex flex-col p-4 gap-4">
        <FeedHeader
          v-if="feed"
          :author="feed.author"
          :createdAt="feed.createdAt"
          :mine="feed.mine"
          @edit="handleEdit"
          @delete="handleDelete"
        />
        <span class="content">{{ feed?.content }}</span>
        <CommentSection v-if="feed" :target-id="feed.id" target-type="FEED" />
      </div>

      <!-- 닫기 버튼 -->
      <button @click="close" class="absolute top-2 right-3 text-xl text-gray-500">×</button>
    </div>
    <UploadGuideModal
      v-if="showImageEditModal"
      :initialImages="feed.imageUrls"
      @fileSelected="handleImageEditSave"
      @close="showImageEditModal = false"
    />

    <FeedUploadModal
      v-if="showFeedEditModal"
      v-model:caption="caption"
      :imageUrls="editImageUrls"
      @upload="handleFeedEdit"
      @close="showFeedEditModal = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { deleteFeed, editFeed, fetchFeed, uploadImages } from '@/api/feed.js';

import FeedCarousel from '../components/FeedCarousel.vue';
import FeedHeader from '../components/FeedHeader.vue';
import CommentSection from '@/components/CommentSection.vue';
import { startLoading } from '@/composable/useLoadingBar.js';
import { likeFeed, unLikeFeed } from '@/api/like.js';
import { showSuccessToast } from '@/utills/toast.js';
import { useFeedRefreshStore } from '@/stores/feedRefreshStore.js';
import UploadGuideModal from '@/components/modal/UploadGuideModal.vue';
import FeedUploadModal from '@/features/feed/components/FeedUploadModal.vue';
import ShareDropdown from '@/components/ShareDropdown.vue';

const feed = ref(null);
const route = useRoute();
const router = useRouter();
const liked = ref(false);
const likeCount = ref(0);
const feedRefreshStore = useFeedRefreshStore();
const showImageEditModal = ref(false);
const showFeedEditModal = ref(false);
const editImageFiles = ref([]);
const editImageUrls = ref([]);
const caption = ref('');

watch(feed, (newFeed) => {
  if (newFeed) {
    liked.value = newFeed.liked;
    likeCount.value = newFeed.likeCount;
    caption.value = newFeed.content;
  }
});

const close = () => {
  router.back();
};

const animateLike = ref(false);
const toggleLike = async () => {
  const payload = {
    targetId: route.params.id,
    targetType: 'FEED',
  };

  try {
    if (liked.value) {
      await unLikeFeed(payload);
      likeCount.value -= 1;
    } else {
      await likeFeed(payload);
      likeCount.value += 1;
    }
    liked.value = !liked.value;

    animateLike.value = true;
    setTimeout(() => {
      animateLike.value = false;
    }, 200);
  } catch (e) {
    console.error('좋아요 처리 실패:', e);
  }
};

onMounted(async () => {
  const feedId = route.params.id;
  startLoading();
  try {
    const res = await fetchFeed(feedId);
    if (res.data.success) {
      feed.value = res.data.data;
    }
  } catch (e) {
    console.error('피드 조회 실패', e);
  }
});

const handleDelete = async () => {
  const confirmDelete = confirm('정말 삭제하시겠습니까?');
  if (!confirmDelete) return;

  try {
    await deleteFeed(feed.value.id);
    showSuccessToast('삭제되었습니다.');
    feedRefreshStore.triggerRefresh();
    router.back();
  } catch (e) {
    console.error(e);
    alert('삭제 중 오류 발생');
  }
};

const handleEdit = () => {
  showImageEditModal.value = true;
};

const handleImageEditSave = ({ existingUrls = [], files = [] }) => {
  editImageFiles.value = files;
  editImageUrls.value = [...existingUrls, ...files.map((f) => URL.createObjectURL(f))];
  showImageEditModal.value = false;
  showFeedEditModal.value = true;
};

const handleFeedEdit = async () => {
  try {
    startLoading();

    // 1. 이미지 업로드
    let uploadedUrls = [];
    if (editImageFiles.value.length > 0) {
      const formData = new FormData();
      editImageFiles.value.forEach((file) => formData.append('files', file));
      const res = await uploadImages(formData);
      uploadedUrls = res.data.data;
    }

    // 2. 기존 이미지 유지
    const original = feed.value?.imageUrls || [];
    const keepUrls = editImageUrls.value.filter((url) => original.includes(url));
    const finalImageUrls = [...keepUrls, ...uploadedUrls];

    // 3. 수정 요청
    const payload = {
      content: caption.value,
      imageUrls: finalImageUrls,
    };

    await editFeed(feed.value.id, payload);
    showSuccessToast('피드가 수정되었습니다!');

    feedRefreshStore.triggerRefresh();
    showFeedEditModal.value = false;

    close();
  } catch (e) {
    console.error('피드 수정 실패:', e);
    alert('피드 수정 중 오류가 발생했습니다.');
  }
};
</script>

<style scoped>
.content {
  @apply text-sm text-gray-800 px-4 mb-4;
}
</style>
