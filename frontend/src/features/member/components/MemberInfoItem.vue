<script setup>
const props = defineProps({
  title: String,
  content: String,
});
</script>

<template>
  <div class="item-frame">
    <div class="title-frame">
      <span class="text1">{{ title }}</span>
    </div>
    <div class="content-frame">
      <span class="text2">{{ content }}</span>
    </div>
  </div>
</template>

<style scoped>
.item-frame {
  height: 45px;
  width: 515px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.title-frame {
  height: 18px;
  width: 515px;
  display: flex;
  flex-direction: column;
}

.title-frame .text1 {
  color: #888888;
  text-align: left;
  vertical-align: text-top;
  font-size: 14px;
  line-height: 1.5;
  font-weight: bold;
  outline: none;
  width: 515px;
}

.content-frame {
  height: 21px;
  width: 515px;
  display: flex;
  flex-direction: column;
}

.content-frame .text2 {
  color: #333333;
  text-align: left;
  vertical-align: text-top;
  font-size: 16px;
  line-height: 1.5;
  border-style: hidden;
  outline: none;
  width: 515px;
}
</style>
