<template>
  <div class="comment-preview">
    <p
      v-if="commentCount > 0"
      class="mb-1 text-gray-400 cursor-pointer hover:underline"
      @click="goToDetail"
    >
      댓글 {{ commentCount }}개 모두 보기
    </p>
    <p v-if="commentPreview" class="flex gap-2">
      <span class="font-semibold">{{ commentPreview.userNickname }}</span>
      <span>{{ commentPreview.content }}</span>
    </p>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const props = defineProps({
  commentCount: Number,
  commentPreview: Object,
  feedId: Number,
});
const router = useRouter();

const goToDetail = () => {
  router.push(`/feed/${props.feedId}`);
};
</script>

<style scoped>
.comment-preview {
  @apply text-xs text-gray-700 mt-6 px-4 pb-6;
}
</style>
