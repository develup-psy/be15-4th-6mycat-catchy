import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUploadStore = defineStore(
  'upload',
  () => {
    const selectedFile = ref(null);

    function setFile(file) {
      selectedFile.value = file;
    }

    function reset() {
      selectedFile.value = null;
    }

    return {
      selectedFile,
      setFile,
      reset,
    };
  },
  {
    persist: true, // pinia-plugin-persistedstate 적용
  },
);
