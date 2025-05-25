<template>
  <div
    ref="scrollContainer"
    class="flex flex-col gap-6 items-center p-6 max-h-[100vh] overflow-y-auto relative scrollbar-hide"
  >
    <router-view />
    <FeedCard v-for="feed in feeds" :key="feed.id" :feed="feed" />
    <p v-if="isLoading" class="loading">불러오는 중...</p>
    <p v-if="!isLoading && feeds.length === 0" class="empty">피드가 없습니다.</p>
    <div v-if="isLastPage" class="text-gray-400 text-sm text-center py-2">catchy</div>

    <button
      v-show="showScrollTop"
      @click="scrollToTop"
      class="fixed bottom-6 right-6 w-12 h-12 bg-primary text-white rounded-full shadow-lg hover:bg-primary-hover flex items-center justify-center z-50"
    >
      <i class="fa-solid fa-arrow-up"></i>
    </button>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue';
import FeedCard from '../components/FeedCard.vue';
import { fetchFeedList } from '@/api/feed.js';
import { useFeedRefreshStore } from '@/stores/feedRefreshStore.js';
import { useInfiniteScroll } from '@/composable/useInfiniteScroll.js';
import { startLoading, isLoading } from '@/composable/useLoadingBar.js';

const scrollContainer = ref(null);
const feedRefreshStore = useFeedRefreshStore();

const fetchFn = async (page) => {
  try {
    startLoading();
    const { data } = await fetchFeedList(page);
    return data;
  } catch (e) {
    console.log(e + '피드 목록 초기 로드 실패');
  }
};

const {
  items: feeds,
  loadMore,
  reset,
  isLastPage,
} = useInfiniteScroll({
  fetchFn,
  scrollTargetRef: scrollContainer,
});

watch(
  () => feedRefreshStore.needsRefresh,
  async (shouldRefresh) => {
    if (shouldRefresh) {
      feedRefreshStore.clearRefresh();
      reset();
      await loadMore();
    }
  },
);

onMounted(() => {
  startLoading();
  scrollContainer.value?.addEventListener('scroll', handleScroll);
});

onBeforeUnmount(() => {
  scrollContainer.value?.removeEventListener('scroll', handleScroll);
});

const showScrollTop = ref(false);

const handleScroll = () => {
  const el = scrollContainer.value;
  if (!el) return;
  showScrollTop.value = el.scrollTop > 300;
};

const scrollToTop = () => {
  scrollContainer.value?.scrollTo({ top: 0, behavior: 'smooth' });
};
</script>

<style scoped>
.loading {
  @apply mt-6 text-gray-500 text-sm;
}

.empty {
  @apply mt-10 text-gray-500 text-sm;
}

.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
.scrollbar-hide {
  -ms-overflow-style: none; /* IE/Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
