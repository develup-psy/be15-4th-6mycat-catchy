<script setup>
const emit = defineEmits(['close']);
</script>

<template>
  <div class="overlay">
    <div class="modal">
      <button class="close" @click="emit('close')">x</button>
      <slot />
    </div>
  </div>
</template>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  position: relative; /* 닫기 버튼 위치 기준 */
  background-color: #ffffff;
  border-radius: 16px;
  padding: 20px;
  min-width: 400px;
  box-shadow: 0px 10px 30px rgba(0, 0, 0, 0.1);
}

.close {
  position: absolute;
  top: 12px;
  right: 12px;
  border: none;
  background: none;
  font-size: 20px;
  cursor: pointer;
}
</style>
