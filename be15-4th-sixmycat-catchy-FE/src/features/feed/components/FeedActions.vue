<template>
  <div class="feed-action">
    <div class="flex items-center gap-4">
      <div class="action-wrapper">
        <i
          :class="[
            liked ? 'fa-solid' : 'fa-regular',
            'fa-heart cursor-pointer transition-transform duration-200',
            animateLike ? 'scale-150' : '',
          ]"
          @click="toggleLike"
        />
        <span class="inline-block text-center w-3">{{ likeCount }}</span>
      </div>
      <div class="action-wrapper">
        <i class="fa-regular fa-comment"></i>
        <span class="inline-block text-center w-3">{{ commentCount }}</span>
      </div>
    </div>
    <ShareDropdown
      :shareUrl="`https://catchy.site/feed`"
      :shareText="props.shareContent"
      :shareImage="props.shareImage"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { likeFeed, unLikeFeed } from '@/api/like.js';
import ShareDropdown from '@/components/ShareDropdown.vue';

const props = defineProps({
  likeCount: Number,
  commentCount: Number,
  liked: Boolean,
  feedId: Number,
  shareContent: String,
  shareImage: String,
});

const liked = ref(props.liked);
const likeCount = ref(props.likeCount);
const animateLike = ref(false);
const toggleLike = async () => {
  const payload = {
    targetId: props.feedId, // ← 부모 컴포넌트에서 넘겨줘야 함
    targetType: 'FEED',
  };

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
    setTimeout(() => {
      animateLike.value = false;
    }, 200);
  } catch (e) {
    console.error('좋아요 처리 실패:', e);
  }
};
</script>

<style scoped>
.feed-action {
  @apply flex justify-between gap-4 p-4 text-primary text-body-sm;
}
.action-wrapper {
  @apply flex items-center gap-2;
}
</style>
