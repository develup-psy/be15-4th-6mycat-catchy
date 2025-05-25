<script setup>
import { ref } from 'vue';
import FeedCarousel from '@/features/feed/components/FeedCarousel.vue';

const props = defineProps({
  imageUrls: { type: Array, required: true },
});

const currentIndex = ref(0);
const caption = defineModel('caption');

const next = () => {
  if (currentIndex.value < props.imageUrls.length - 1) {
    currentIndex.value++;
  }
};
const prev = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  }
};

const emit = defineEmits(['close', 'upload']);
</script>

<template>
  <section class="overlay" @click.self="emit('close')">
    <article class="reel-modal">
      <FeedCarousel :images="props.imageUrls" />

      <!-- 설명 입력 -->
      <form class="reel-form" @submit.prevent="emit('upload')">
        <div class="input-group">
          <label for="reel-caption" class="label">피드 설명</label>
          <textarea
            id="reel-caption"
            class="textarea"
            placeholder="피드 설명을 입력하세요"
            v-model="caption"
          ></textarea>
        </div>
        <button type="submit" class="submit-button">피드 업로드</button>
      </form>
    </article>
  </section>
</template>
<style scoped>
.overlay {
  @apply fixed inset-0 bg-black-alpha-60 flex justify-center items-center z-50;
}

.reel-modal {
  @apply w-[860px] h-[560px] flex bg-white rounded-lg overflow-hidden shadow-elevated;
}

.reel-form {
  @apply w-[300px] p-6 flex flex-col justify-between;
}

.input-group {
  @apply flex flex-col gap-1 mt-3;
}

.label {
  @apply text-body-sm text-gray-700;
}

.textarea {
  @apply w-full h-[150px] resize-none border border-gray-300 rounded-md p-2 text-body-sm;
}

.submit-button {
  @apply bg-primary text-white text-body-sm py-3 px-4 rounded-md mt-4 hover:bg-primary-hover;
}
</style>
