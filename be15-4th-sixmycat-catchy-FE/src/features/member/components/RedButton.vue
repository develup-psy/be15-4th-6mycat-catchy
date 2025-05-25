<!-- RedButton.vue -->
<script setup>
defineEmits(['click']);
</script>

<template>
  <button class="red-button" @click="$emit('click')">
    <slot></slot>
  </button>
</template>

<style scoped>
.red-button {
  background-color: #ff4d4f;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  width: fit-content;
}
</style>
