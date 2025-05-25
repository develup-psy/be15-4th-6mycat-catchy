<script setup>
import { ref, watch } from 'vue';
import { fetchLikedFeedList, fetchMyFeedList } from '@/api/feed.js';
import { fetchLikedJjureList, fetchMyJjureList } from '@/api/jjure.js';
import { useInfiniteScroll } from '@/composable/useInfiniteScroll.js';
import ThumbnailItem from '@/features/profile/components/ThumbnailItem.vue';

const { selectedTab } = defineProps({
  selectedTab: String,
});

const scrollContainer = ref(null);

const fetchMap = {
  MyFeed: fetchMyFeedList,
  LikedFeed: fetchLikedFeedList,
  MyJjure: fetchMyJjureList,
  LikedJjure: fetchLikedJjureList,
};

const fetchFn = async (page = 1) => {
  try {
    const fetchFn = fetchMap[selectedTab];
    if (!fetchFn) {
      console.warn('해당하는 selectedTab이 없습니다.');
      items.value = [];
      return;
    }

    const { data } = await fetchFn({ page, size: 9 });
    return data;
  } catch (error) {
    console.log('API 오류:', error);
    items.value = [];
  }
};

const {
  items: items,
  isLoading,
  isLastPage,
  reset,
} = useInfiniteScroll({
  fetchFn,
  scrollTargetRef: scrollContainer,
});

watch(
  () => selectedTab,
  () => {
    reset();
  },
);
</script>

<template>
  <div class="thumbnail-scroll" ref="scrollContainer">
    <!-- 데이터 없을 때 -->
    <div v-if="items.length === 0" class="text-gray-400 text-sm text-center py-2">
      데이터가 없습니다.
    </div>
    <!-- 데이터 있을 때 -->
    <div v-else class="grid-body">
      <ThumbnailItem
        v-for="(item, index) in items"
        :key="index"
        :item="item"
        :selected-tab="selectedTab"
      />
    </div>
    <div class="w-full text-center py-4 text-gray-400 text-sm">
      <span v-if="isLoading">불러오는 중...</span>
      <span v-else-if="isLastPage">catchy</span>
    </div>
  </div>
</template>

<style scoped>
.thumbnail-scroll {
  @apply h-[300px] overflow-y-auto;
  -ms-overflow-style: none;
}

.thumbnail-scroll::-webkit-scrollbar {
  display: none;
}

.grid-body {
  @apply grid grid-cols-3 gap-3;
}
</style>
