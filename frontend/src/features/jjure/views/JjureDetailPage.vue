<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { fetchJjureDetail } from '@/api/jjure.js';
import CommentSection from '@/components/CommentSection.vue';
import { likeFeed, unLikeFeed } from '@/api/like.js';
import { startLoading, stopLoading } from '@/composable/useLoadingBar.js';
import ShareDropdown from '@/components/ShareDropdown.vue';

const jjure = ref(null);
const route = useRoute();
const router = useRouter();
const jjureId = route.params.id;

const liked = ref(false);
const likeCount = ref(0);
const animateLike = ref(false);
const videoUrl = ref(null);

// 닫기
const close = () => router.back();

// 좋아요 토글
const toggleLike = async () => {
  const payload = { targetId: jjureId, targetType: 'JJURE' };
  try {
    if (liked.value) {
      await unLikeFeed(payload);
      likeCount.value -= 1;
    } else {
      await likeFeed(payload);
      likeCount.value += 1;
    }
    liked.value = !liked.value;
    animateLike.value = true;
    setTimeout(() => (animateLike.value = false), 200);
  } catch (e) {
    console.error('좋아요 실패:', e);
  }
};

watch(
  jjure,
  (val) => {
    if (val?.fileKey) {
      const key = val.fileKey;
      const bucket = import.meta.env.VITE_AWS_BUCKET_NAME;
      const region = import.meta.env.VITE_AWS_REGION;
      videoUrl.value = `https://${bucket}.s3.${region}.amazonaws.com/${key}`;
      console.log('📽️ videoUrl 설정됨:', videoUrl.value);
    }
  },
  { immediate: true },
);

const commentSectionRef = ref(null);

const focusCommentInput = async () => {
  let tryCount = 0;
  while (!commentSectionRef.value?.focusInput && tryCount < 10) {
    await nextTick();
    tryCount++;
  }
  await nextTick(); // 렌더 완료 보장
  if (commentSectionRef.value?.focusInput) {
    commentSectionRef.value.focusInput();
  } else {
    console.warn('커멘트 섹션 작동 안함');
  }
};

// 상세 조회
onMounted(async () => {
  startLoading();
  try {
    const res = await fetchJjureDetail(jjureId);
    if (res.data.success) {
      jjure.value = res.data.data;
      liked.value = jjure.value.liked;
      likeCount.value = jjure.value.likeCount;
    }
  } catch (e) {
    console.error('쭈르 상세 조회 실패:', e);
  } finally {
    stopLoading();
  }
});
</script>

<template>
  <div class="backdrop">
    <div class="modal-container">
      <!-- 비디오 -->
      <div class="video-area">
        <video
          v-if="videoUrl"
          :key="videoUrl"
          controls
          autoplay
          muted
          playsinline
          loop
          class="video-player"
        >
          <source :src="videoUrl" type="video/mp4" />
        </video>

        <div class="overlay-actions">
          <button class="overlay-button icon-wrapper" @click="toggleLike">
            <i
              :class="[
                liked ? 'fa-solid' : 'fa-regular',
                'fa-heart cursor-pointer transition-transform duration-200',
                animateLike ? 'scale-150' : '',
              ]"
            />
            <span>{{ likeCount }}</span>
          </button>
          <button class="overlay-button icon-wrapper" @click="focusCommentInput">
            <i class="fa-regular fa-comment"></i>
            <span>{{ jjure?.commentCount }}</span>
          </button>
          <button class="overlay-button icon-wrapper">
            <ShareDropdown
              :shareUrl="`http://localhost:5173/jjure/${jjure?.id}`"
              :shareText="jjure?.caption"
              :shareImage="jjure?.thumbnailUrl"
            />
          </button>
        </div>
      </div>

      <!-- 우측: 작성자 / 댓글 -->
      <div class="right-panel">
        <div class="author-info">
          <img :src="jjure?.author.profileImageUrl" alt="프로필" class="author-image" />
          <span class="author-name">@{{ jjure?.author.nickname }}</span>
        </div>
        <p class="caption-text">{{ jjure?.caption }}</p>

        <!-- 댓글 -->
        <CommentSection
          v-if="jjure"
          :target-id="jjure.id"
          target-type="JJURE"
          :ref="commentSectionRef"
        />
      </div>

      <!-- 닫기 -->
      <button @click="close" class="close-button">×</button>
    </div>
  </div>
</template>

<style scoped>
.backdrop {
  @apply fixed inset-0 bg-black-alpha-60 flex justify-center items-center z-50;
}

.icon-wrapper {
  @apply cursor-pointer text-center shadow-elevated bg-black-alpha-20 rounded-full p-2;
}

.modal-container {
  @apply bg-white w-[90%] max-w-4xl h-[700px] rounded-xl flex overflow-hidden relative;
}

.video-area {
  @apply w-[450px] bg-black flex items-center justify-center relative;
}

.video-player {
  @apply w-full h-full object-cover rounded;
}

.overlay-actions {
  @apply absolute right-2 bottom-14 flex flex-col items-center gap-4 text-primary text-sm p-2;
}

.overlay-button {
  @apply flex flex-col items-center opacity-80 hover:opacity-100;
}

.right-panel {
  @apply flex-1 flex flex-col p-4 gap-4;
}

.author-info {
  @apply flex items-center gap-2;
}

.author-image {
  @apply w-8 h-8 rounded-full;
}

.author-name {
  @apply font-bold text-sm text-primary;
}

.caption-text {
  @apply text-sm text-gray-700;
}

.close-button {
  @apply absolute top-2 right-3 text-xl text-gray-500;
}
</style>
