<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { fetchJjureList } from '@/api/jjure.js';
import JjureCard from '@/features/jjure/components/JjureCard.vue';
import { startLoading } from '@/composable/useLoadingBar.js';
import { useUploadStore } from '@/stores/uploadStore.js';
import { useInfiniteScroll } from '@/composable/useInfiniteScroll.js';

const scrollTargetRef = ref(null);

const fetchFn = async (page) => {
  try {
    const size = 5;
    const sendedPage = page - 1;
    startLoading();
    const { data } = await fetchJjureList({ page: sendedPage, size });
    console.log("더미 데이터 추가")
    return data;
  } catch (e) {
    console.log(e + '알림 목록 초기 로드 실패');
  }
};

const {
  items: jjures,
  isLastPage,
  reset,
} = useInfiniteScroll({
  fetchFn,
  scrollTargetRef,
});

const uploadStore = useUploadStore();

watch(
  () => uploadStore.selectedFile,
  async (newVal, oldVal) => {
    if (oldVal !== null && newVal === null) {
      await reset();
    }
  },
);

onMounted(async () => {
  startLoading();
});
</script>

<template>
  <main class="jjure-list-page">
    <section class="jjure-list" ref="scrollTargetRef">
      <JjureCard
        v-for="(jjure, index) in jjures"
        :key="jjure.id"
        :id="jjure.id"
        :file-key="jjure.fileKey"
        :author-nickname="jjure.author.nickname"
        :author-image-url="jjure.author.profileImageUrl"
        :caption="jjure.caption"
        :music-title="jjure.musicUrl"
        :comment-preview="jjure.commentPreview?.content"
        :like-count="jjure.likeCount"
        :comment-count="jjure.commentCount"
        :isLiked="jjure.liked"
        :isMine="jjure.mine"
        :thumbnailUrl="jjure.thumbnailUrl"
      />
      <div v-if="isLastPage" class="text-gray-400 text-sm text-center py-2">catchy</div>
    </section>

    <p v-if="isLoading" class="loading">불러오는 중...</p>
    <p v-if="!isLoading && jjures.length === 0" class="empty">쭈르가 없습니다.</p>

    <router-view />
  </main>
</template>

<style scoped>
.jjure-list-page {
  @apply flex flex-col items-center px-4 py-10 h-[100vh];
}

.jjure-list {
  @apply gap-6 overflow-y-auto w-full flex flex-col gap-4;
  -ms-overflow-style: none;
}

.jjure-list::-webkit-scrollbar {
  display: none;
}

.loading {
  @apply mt-6 text-gray-500 text-sm;
}

.empty {
  @apply mt-10 text-gray-500 text-sm;
}
</style>
