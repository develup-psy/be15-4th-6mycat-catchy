<script setup>
const props = defineProps({
  text: String,
  iconSrc: String,
  redirectUrl: String
});

const handleClick = () => {
  window.location.href = props.redirectUrl;
};
</script>

<template>
  <button class="button" @click="handleClick">
    <img class="icon" :src="props.iconSrc" alt="icon" />
    <span class="text">{{ props.text }}</span>
  </button>
</template>

<style scoped>
.button {
  background-color: #ffffff;
  height: 40px;
  width: 182px;
  border-radius: 8px;
  padding: 11px 20px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  border: 1.5px solid #FF5C8D;
}

.icon {
  width: 20px;
  height: 20px;
}

.text {
  font-size: 14px;
  font-weight: 500;
  color: #FF5C8D;
  margin-left: 10px;
}
</style>
