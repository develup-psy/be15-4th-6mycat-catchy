// src/composables/useInfiniteScroll.js
import { ref, onMounted, onBeforeUnmount } from 'vue';

export function useInfiniteScroll({ fetchFn, scrollTargetRef, threshold = 200 }) {
  const items = ref([]);
  const curPage = ref(1);
  const totalPage = ref(1);
  const isLoading = ref(false);
  const isLastPage = ref(false);

  const fetchInitial = async () => {
    try {
      isLoading.value = true;
      const wrapper = await fetchFn(1);
      items.value = wrapper.data.content;
      curPage.value = wrapper.data.currentPage + 1;
      totalPage.value = wrapper.data.totalPages;
      if (wrapper.data.currentPage + 1 === wrapper.data.totalPages) {
        isLastPage.value = true;
      }
    } finally {
      isLoading.value = false;
    }
  };

  const loadMore = async () => {
    if (isLoading.value || isLastPage.value) return;

    try {
      isLoading.value = true;
      const wrapper = await fetchFn(curPage.value + 1);
      items.value.push(...wrapper.data.content);
      curPage.value = wrapper.data.currentPage + 1;
      if (wrapper.data.currentPage + 1 === wrapper.data.totalPages) {
        isLastPage.value = true;
      }
    } finally {
      isLoading.value = false;
    }
  };

  const handleScroll = async () => {
    const el = scrollTargetRef.value;
    if (!el || isLoading.value || isLastPage.value) return;

    const { scrollTop, scrollHeight, clientHeight } = el;
    if (scrollTop + clientHeight >= scrollHeight - threshold) {
      await loadMore();
    }
  };

  onMounted(async () => {
    await fetchInitial();
    scrollTargetRef.value?.addEventListener('scroll', handleScroll);
  });

  onBeforeUnmount(() => {
    scrollTargetRef.value?.removeEventListener('scroll', handleScroll);
  });

  const reset = async () => {
    curPage.value = 1;
    totalPage.value = 1;
    items.value = [];
    isLastPage.value = false;
    await fetchInitial();

    // scroll 이벤트가 살아 있는지 확인해서 없으면 다시 붙이기
    const el = scrollTargetRef.value;
    if (el) {
      el.removeEventListener('scroll', handleScroll); // 중복 방지
      el.addEventListener('scroll', handleScroll);
    }
  };

  return {
    items,
    isLoading,
    isLastPage,
    reset, // 외부에서 호출 가능하도록
    loadMore,
  };
}
