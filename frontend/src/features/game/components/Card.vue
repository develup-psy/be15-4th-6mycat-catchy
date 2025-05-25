<script setup>
const props = defineProps({
  card: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['click']);

function handleClick() {
  emit('click');
}
</script>

<template>
  <div class="card-container" @click="handleClick">
    <div :class="['card-inner', card.isFlipped || card.isMatched ? 'flipped' : '']">
      <div class="card-face card-front">
        <img :src="card.img" alt="고양이 앞면" class="card-image" />
      </div>
      <div class="card-face card-back">
        <img src="@/assets/logo.png" alt="카드 뒷면" class="card-image" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.card-container {
  @apply w-[100px] h-[140px];
  perspective: 1000px;
}

.card-inner {
  @apply relative w-full h-full transition-transform duration-500 rounded-md bg-white;
  transform-style: preserve-3d;
}

.card-inner.flipped {
  transform: rotateY(180deg);
}

.card-face {
  @apply absolute w-full h-full overflow-hidden rounded-lg p-2;
  backface-visibility: hidden;
}

.card-front {
  transform: rotateY(180deg);
  z-index: 2;
}

.card-back {
  z-index: 1;
}

.card-image {
  @apply w-full h-full object-contain;
}
</style>
