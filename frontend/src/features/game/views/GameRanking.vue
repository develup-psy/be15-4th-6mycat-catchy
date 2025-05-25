<script setup>
import { ref, onMounted, nextTick, watch } from 'vue';
import { useRouter } from 'vue-router';
import { getGameRanking } from '@/api/game.js';
import { useGameStore } from '@/stores/useGameStore.js';
import { showErrorToast } from '@/utills/toast.js';
import { useAuthStore } from '@/stores/auth.js';

const router = useRouter();
const gameStore = useGameStore();
const auth = useAuthStore();
const limit = 10;

const isFail = ref(false);
const resultTime = ref(null);
const bestScore = ref(null);
const topPercentage = ref(null);
const rankers = ref([]);

onMounted(async () => {
  await nextTick();

  // // 게임 상태 없으면 되돌리기
  // if (gameStore.gameStatus == null) {
  //   router.push('/game/start');
  //   return;
  // }

  isFail.value = gameStore.gameStatus === 'fail';
  resultTime.value = gameStore.resultTime;

  try {
    const response = await getGameRanking(auth.memberId, limit);
    const data = response.data.data;

    bestScore.value = data.myScore;
    topPercentage.value = data.topPercentage;
    rankers.value = data.topRankers.sort((a, b) => a.score - b.score);
  } catch (err) {
    console.error('랭킹 조회 실패:', err);
    showErrorToast('랭킹 조회에 실패했다냥...ㅠ');
  }
});

const shareUrl = window.location.href;
const shareText = ref('');

watch(resultTime, (score) => {
  if (score !== null) {
    shareText.value = `내가 Catch Me! 게임에서 ${Math.floor(score / 1000)}초 기록! 너도 도전해봐!`;
  }
});

const shareKakao = () => {
  if (!window.Kakao.isInitialized()) {
    window.Kakao.init(import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY);
  }

  window.Kakao.Share.sendDefault({
    objectType: 'feed',
    content: {
      title: shareText.value,
      description: 'CATCHY에서 작성된 내용입니다.',
      imageUrl: '',
      link: {
        mobileWebUrl: '',
        webUrl: '',
      },
    },
    buttons: [
      {
        title: '내용 보기',
        link: {
          mobileWebUrl: '',
          webUrl: '',
        },
      },
    ],
  });
};

function copyLink() {
  navigator.clipboard.writeText(shareUrl).then(() => {
    alert('링크가 복사되었습니다!');
  });
}

function getRankClass(rank, isMe) {
  const base = ['rank-card'];
  if (rank === 1) base.push('rank-1');
  if (rank === 2) base.push('rank-2');
  if (rank === 3) base.push('rank-3');
  if (isMe) base.push('my-rank');
  return base.join(' ');
}
</script>

<template>
  <main class="ranking-container">
    <h1 class="ranking-title">Catch Me! 게임 랭킹</h1>

    <section class="result-box">
      <p class="result-text">
        최신 기록: <strong>{{ Math.floor(resultTime / 1000) }}초</strong>
      </p>
      <p class="result-status">
        결과:
        <strong :class="isFail ? 'text-red-500' : 'text-green-500'">
          {{ isFail ? '실패ㅠㅠ' : '성공!' }}
        </strong>
      </p>
    </section>

    <section class="score-box">
      <p class="my-score">
        역대 최고 기록: <strong>{{ Math.floor(bestScore / 1000) }}초</strong>
      </p>
      <p class="percent-rank" v-if="!isFail">
        상위 <strong>{{ Math.round(topPercentage) }}%</strong>에 속합니다
      </p>
      <p class="percent-rank" v-else>아쉽다냥...</p>
      <div class="share-section">
        <button @click="shareKakao" class="sns-btn kakao">k</button>
        <button @click="copyLink" class="sns-btn link">🔗</button>
      </div>
    </section>

    <ul class="rank-list">
      <li
        v-for="ranker in rankers"
        :key="ranker.rank"
        :class="getRankClass(ranker.rank, !isFail && ranker.id == auth.memberId)"
      >
        <div class="flex items-center gap-2">
          <i v-if="ranker.rank === 1" class="fas fa-crown text-yellow-400 text-[18px]" />
          <span class="rank-text">
            {{ ranker.rank }}위
            <span v-if="!isFail && ranker.id == auth.memberId" class="ml-1 text-pink-400 font-bold"
              >(나)</span
            >
          </span>
        </div>
        <span class="score-text">{{ Math.floor(ranker.score / 1000) }}초</span>
      </li>
    </ul>

    <button class="restart-btn" @click="router.push('/game/start')">다시 시작하기</button>
  </main>
</template>

<style scoped>
.ranking-container {
  @apply flex flex-col items-center px-4 py-12 bg-gray-100 font-sans;
}

.ranking-title {
  @apply text-[22px] font-bold text-gray-600 mb-6;
}

.result-box {
  @apply bg-white rounded-xl shadow px-6 py-5 max-w-[340px] w-full text-center mb-4;
}

.result-text {
  @apply text-[14px] text-gray-600 mb-1;
}

.result-status {
  @apply text-[16px] font-bold mb-1;
}

.score-box {
  @apply bg-white rounded-xl shadow px-6 py-5 max-w-[340px] w-full text-center mb-6;
}

.my-score {
  @apply text-[14px] text-gray-600 mb-2;
}

.percent-rank {
  @apply text-[16px] font-bold text-gray-600 mb-3;
}

.percent-rank strong {
  @apply text-yellow-400;
}

.share-section {
  @apply flex justify-center gap-3 mt-3;
}

.sns-btn {
  @apply w-9 h-9 rounded-full text-white font-bold flex items-center justify-center cursor-pointer transition;
}

.kakao {
  @apply bg-yellow-300 hover:bg-yellow-400 text-black;
}

.link {
  @apply bg-gray-500 hover:bg-gray-600;
}

.rank-list {
  @apply flex flex-col gap-3 w-full max-w-[340px] mb-8;
}

.rank-card {
  @apply flex justify-between items-center bg-white px-5 py-3 rounded-md shadow text-[14px] text-gray-700;
}

.rank-1 {
  @apply border-2 border-yellow-400;
}

.rank-2 {
  @apply bg-yellow-50;
}

.rank-3 {
  @apply bg-gray-100;
}

.my-rank {
  @apply bg-pink-50 border-2 border-pink-400;
}

.rank-text {
  @apply font-semibold flex items-center gap-1;
}

.score-text {
  @apply text-gray-500;
}

.restart-btn {
  @apply mt-2 px-5 py-2 text-[14px] bg-pink-500 text-white font-semibold rounded-md hover:bg-pink-600 transition;
}
</style>
