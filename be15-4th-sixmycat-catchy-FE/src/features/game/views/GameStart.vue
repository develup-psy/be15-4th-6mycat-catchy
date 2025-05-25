<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ImageSelectModal from '@/features/game/components/ImageSelectModal.vue';
import { useGameStore } from '@/stores/useGameStore.js';

const showModal = ref(false);
const gameStore = useGameStore();
const router = useRouter();

function handleOpenModal() {
  showModal.value = true;
}

function handleImagesSelected(files) {
  const fileList = [...files].slice(0, 8); // 최대 8장 제한
  gameStore.setSelectedFiles(fileList); // File 객체 그대로 저장
  showModal.value = false;
  router.push('/game/play');
}
</script>

<template>
  <main class="game-start-wrapper">
    <section class="start-container">
      <h1 class="title">🎮 Catch Me 카드 게임</h1>
      <p class="description">카드를 뒤집어 같은 고양이를 찾아보세요!</p>
      <button class="start-button" @click="handleOpenModal">카드 이미지 선택하기</button>
    </section>

    <ImageSelectModal
      v-if="showModal"
      @close="showModal = false"
      @imagesSelected="handleImagesSelected"
    />
  </main>
</template>

<style scoped>
.game-start-wrapper {
  @apply flex items-center justify-center min-h-screen bg-gray-50;
}

.start-container {
  @apply bg-white border border-pink-300 p-10 rounded-lg text-center shadow-lg max-w-md;
}

.title {
  @apply text-2xl text-pink-500 font-bold mb-4;
}

.description {
  @apply text-gray-600 mb-6;
}

.start-button {
  @apply px-6 py-3 text-base bg-pink-500 text-white rounded-md cursor-pointer hover:bg-pink-600;
}
</style>
