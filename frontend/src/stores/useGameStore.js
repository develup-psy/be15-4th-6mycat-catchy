// useGameStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useGameStore = defineStore('game', () => {
  const selectedFiles = ref([]);
  const imageUrls = ref([]);
  const resultTime = ref(24); // 걸린 시간 (초)
  const gameStatus = ref('success'); // 'success' | 'fail'

  function setSelectedFiles(files) {
    selectedFiles.value = files;
  }

  function setImageUrls(urls) {
    imageUrls.value = urls;
  }

  function setResult({ time, status }) {
    resultTime.value = time;
    gameStatus.value = status; // 'success' 또는 'fail'
  }

  function resetGame() {
    selectedFiles.value = [];
    imageUrls.value = [];
    resultTime.value = null;
    gameStatus.value = null;
  }

  return {
    selectedFiles,
    imageUrls,
    resultTime,
    gameStatus,
    setSelectedFiles,
    setImageUrls,
    setResult,
    resetGame,
  };
});
