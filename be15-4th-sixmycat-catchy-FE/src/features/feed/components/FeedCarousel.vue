<template>
  <div class="feed-carousel">
    <img :src="images[currentIndex]" @click="goToDetail" alt="feed-image" />

    <!-- 왼쪽 화살표: 첫 번째 사진에서는 숨김 -->
    <button v-if="images.length > 1 && currentIndex > 0" @click="prev" class="prev">
      <i class="fa-solid fa-arrow-left" style="color: #e04f7e"></i>
    </button>

    <!-- 오른쪽 화살표: 마지막 사진에서는 숨김 -->
    <button v-if="images.length > 1 && currentIndex < images.length - 1" @click="next" class="next">
      <i class="fa-solid fa-arrow-right" style="color: #e04f7e"></i>
    </button>

    <!-- 하단 ●●● 인디케이터 -->
    <div v-if="images.length > 1" class="indicator">
      <span
        v-for="(img, i) in images"
        :key="i"
        class="w-2 h-2 rounded-full"
        :class="i === currentIndex ? 'bg-primary-hover' : 'bg-white'"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({ images: Array, feedId: Number });
const currentIndex = ref(0);
const router = useRouter();

const next = () => {
  if (currentIndex.value < props.images.length - 1) currentIndex.value++;
};

const prev = () => {
  if (currentIndex.value > 0) currentIndex.value--;
};

const goToDetail = () => {
  router.push(`/feed/${props.feedId}`);
};
</script>

<style scoped>
.feed-carousel {
  @apply relative w-full h-full max-w-[560px] max-h-[560px] aspect-[5/4] overflow-hidden bg-black/5;
}

.feed-carousel img {
  @apply w-full h-full object-cover;
}

.feed-carousel .prev {
  @apply absolute left-3 top-1/2 -translate-y-1/2 bg-white shadow-md w-8 h-8 rounded-full flex justify-center items-center hover:bg-gray-200 opacity-70;
}
.feed-carousel .next {
  @apply absolute right-3 top-1/2 -translate-y-1/2 bg-white shadow-md w-8 h-8 rounded-full flex justify-center items-center hover:bg-gray-200 opacity-70;
}

.indicator {
  @apply absolute bottom-3 left-1/2 -translate-x-1/2 flex gap-1;
}
</style>
