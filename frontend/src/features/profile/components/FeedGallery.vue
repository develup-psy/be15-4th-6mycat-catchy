<template>
  <div class="grid grid-cols-3 gap-2">
    <img
      v-for="(img, i) in images"
      :key="i"
      :src="img"
      class="rounded object-cover h-32 w-full"
    />
  </div>
</template>

<script setup>
defineProps({
  images: {
    type: Array,
    required: true,
  },
})
</script>