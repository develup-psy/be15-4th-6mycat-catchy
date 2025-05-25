// composables/usePagination.js
import { ref } from 'vue';

export function usePagination(fetchFn) {
  const items = ref([]);
  const page = ref(0);
  const size = ref(10);
  const hasNext = ref(true);
  const isLoading = ref(false);

  const loadMore = async () => {
    if (isLoading.value || !hasNext.value) return;

    isLoading.value = true;
    try {
      const response = await fetchFn(page.value, size.value);
      const { content, last } = response.data.data;
      console.log(content);

      items.value.push(...content);
      hasNext.value = !last;
      page.value += 1;
      isLoading.value = false;
    } catch (e) {
      console.error('페이지네이션 데이터 가져오기 실패', e);
    }
  };

  const reset = () => {
    items.value = [];
    page.value = 0;
    hasNext.value = true;
  };

  return {
    items,
    loadMore,
    reset,
    hasNext,
    isLoading,
  };
}
