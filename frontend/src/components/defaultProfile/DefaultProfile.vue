<script setup>
import { computed } from 'vue';
import { useDefaultProfileStore } from '@/stores/defaultProfileStore.js';
const props = defineProps({
  src: {
    type: String,
    default: '',
  },
  size: {
    type: Number,
    default: 40,
  },
});

const { defaultImage } = useDefaultProfileStore();

const imageSrc = computed(() => {
  return props.src && typeof props.src === 'string' && props.src.trim() !== ''
    ? props.src
    : defaultImage;
});
</script>

<template>
  <div>
    <img
      :src="imageSrc"
      alt="프로필 이미지"
      class="rounded-full object-cover"
      :style="{ width: `${size}px`, height: `${size}px` }"
    />
  </div>
</template>
