<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useGameStore } from '@/stores/useGameStore.js';
import { useRouter } from 'vue-router';
import Card from '@/features/game/components/Card.vue';
import { saveGameScore } from '@/api/game.js';
import { showErrorToast } from '@/utills/toast.js';
import { startLoading } from '@/composable/useLoadingBar.js';

const router = useRouter();
const gameStore = useGameStore();

const cards = ref([]);
const flipped = ref([]);
const startTime = ref(0);
const now = ref(Date.now());
const blobUrls = ref([]); // blob URL 저장소
const maxTimeMs = 60 * 1000; // 60초 제한
let animationFrameId;
let gameEnded = false;

const elapsedMs = computed(() => Math.min(now.value - startTime.value, maxTimeMs));
const progressPercent = computed(() => (elapsedMs.value / maxTimeMs) * 100);

function formatTime(ms) {
  const totalSec = Math.floor(ms / 1000);
  const min = String(Math.floor(totalSec / 60)).padStart(2, '0');
  const sec = String(totalSec % 60).padStart(2, '0');
  const msStr = String(Math.floor((ms % 1000) / 10)).padStart(2, '2');
  return `${min}:${sec}.${msStr}`;
}

function tick() {
  now.value = Date.now();
  if (elapsedMs.value < maxTimeMs && !gameEnded) {
    animationFrameId = requestAnimationFrame(tick);
  } else if (!gameEnded) {
    gameEnded = true;
    cancelAnimationFrame(animationFrameId);

    gameStore.setResult({ time: null, status: 'fail' });
    router.push('/game/ranking');
  }
}

function startTimer() {
  startTime.value = Date.now();
  now.value = Date.now();
  tick();
}

function shuffle(array) {
  return [...array].sort(() => Math.random() - 0.5);
}

function generateCards() {
  const files = gameStore.selectedFiles;

  // 기존 URL 해제
  blobUrls.value.forEach((url) => URL.revokeObjectURL(url));
  blobUrls.value = [];

  const selected = files.map((file) => {
    const url = URL.createObjectURL(file);
    blobUrls.value.push(url);
    return url;
  });

  const needed = 8 - selected.length;

  const defaultImages = import.meta.glob('@/assets/default_images/*.png', {
    eager: true,
    import: 'default',
  });

  const defaultList = Object.values(defaultImages).slice(0, needed);
  const allImages = [...selected, ...defaultList];

  const paired = allImages.flatMap((img, i) => [
    { id: i * 2, value: i, img, isFlipped: false, isMatched: false },
    { id: i * 2 + 1, value: i, img, isFlipped: false, isMatched: false },
  ]);

  cards.value = shuffle(paired);
}

async function handleCardClick(card) {
  if (card.isFlipped || card.isMatched || flipped.value.length === 2) return;

  card.isFlipped = true;
  flipped.value.push(card);

  if (flipped.value.length === 2) {
    const [a, b] = flipped.value;
    if (a.value === b.value) {
      a.isMatched = b.isMatched = true;
      flipped.value = [];

      if (cards.value.every((c) => c.isMatched)) {
        gameEnded = true;
        cancelAnimationFrame(animationFrameId);

        const finishedTimeSec = elapsedMs.value;
        gameStore.setResult({ time: finishedTimeSec, status: 'success' });

        try {
          startLoading();
          await saveGameScore(finishedTimeSec);
        } catch (err) {
          console.error('게임 점수 저장 실패:', err?.response?.data || err.message);
          showErrorToast('게임 점수 저장에 실패했다냥ㅠㅠㅠ 다시 시도해달라냥 ㅠㅠㅠ.');
          return; // 점수 저장 실패 시 랭킹 페이지로 이동하지 않도록 방지
        }

        setTimeout(async () => {
          await router.push('/game/ranking');
        }, 600);
      }
    } else {
      setTimeout(() => {
        a.isFlipped = b.isFlipped = false;
        flipped.value = [];
      }, 800);
    }
  }
}

onMounted(() => {
  generateCards();
  startTimer();
});

onBeforeUnmount(() => {
  cancelAnimationFrame(animationFrameId);
  blobUrls.value.forEach((url) => URL.revokeObjectURL(url));
});
</script>

<template>
  <main class="game-play-wrapper">
    <h1 class="game-title">Catch Me! 고양이 카드 게임</h1>

    <section class="timer-wrapper">
      <div class="timer-bar">
        <div class="timer-fill" :style="{ width: `${progressPercent}%` }"></div>
      </div>
      <p class="timer-label">⏱️ {{ formatTime(elapsedMs) }}</p>
    </section>

    <section class="board">
      <Card
        v-for="card in cards"
        :key="card.id"
        :card="card"
        @click="() => handleCardClick(card)"
      />
    </section>
  </main>
</template>

<style scoped>
.game-play-wrapper {
  @apply min-h-screen py-10 px-5 bg-gray-50 flex flex-col justify-center items-center;
}

.game-title {
  @apply text-[28px] text-gray-700 font-bold mb-8;
}

.timer-wrapper {
  @apply w-[600px] mx-auto mb-6;
}

.timer-bar {
  @apply h-[18px] bg-white border border-gray-200 rounded-[12px] shadow overflow-hidden relative;
}

.timer-fill {
  @apply h-full bg-gradient-to-r from-green-200 via-yellow-200 to-yellow-400 transition-all duration-300 ease-in-out rounded-[12px];
}

.timer-label {
  @apply mt-2 text-sm font-medium text-gray-600 tracking-wider;
}

.board {
  @apply grid grid-cols-4 gap-[20px] w-[550px];
}
</style>
