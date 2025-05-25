import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useFeedRefreshStore = defineStore('feedRefresh', () => {
  const needsRefresh = ref(false);

  const triggerRefresh = () => {
    needsRefresh.value = true;
  };

  const clearRefresh = () => {
    needsRefresh.value = false;
  };

  return {
    needsRefresh,
    triggerRefresh,
    clearRefresh,
  };
});
