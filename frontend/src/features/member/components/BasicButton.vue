<!-- BasicButton.vue -->
<script setup>
const props = defineProps({
  text: { type: String, required: true },
  backgroundColor: { type: String, default: '#ff5c8d' },
  textColor: { type: String, default: '#ffffff' },
});
</script>

<template>
  <button class="signup-button" :style="{ backgroundColor }">
    <span class="signup-text" :style="{ color: textColor }">{{ text }}</span>
  </button>
</template>

<style scoped>
.signup-button {
  height: 44px;
  width: 118px;
  border-radius: 8px;
  padding: 13px 20px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  position: relative;
  border: none;
  cursor: pointer;
}

.signup-text {
  font-size: 14px;
  line-height: 1.5;
  text-align: center;
  vertical-align: middle;
  border: none;
  outline: none;
}
</style>
