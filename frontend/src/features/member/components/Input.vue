<script setup>
const props = defineProps({
  title: String,
  text: String,
  modelValue: String,
  readonly: { type: Boolean, default: false },
});

const emit = defineEmits(['update:modelValue']);
</script>

<template>
  <div class="input-frame">
    <div class="title">{{ title }}</div>
    <div class="text-frame">
      <input
        class="text"
        :placeholder="text"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :readonly="readonly"
      />
    </div>
  </div>
</template>

<style scoped>
.input-frame {
  height: 68px;
  width: 338px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.title {
  font-size: 14px;
  color: #888888;
  margin-bottom: 4px;
}

.text-frame {
  background-color: #ffffff;
  height: 40px;
  width: 337px;
  border-radius: 8px;
  padding-left: 11px;
  padding-right: 11px;
  padding-top: 9px;
  padding-bottom: 9px;
  display: flex;
  flex-direction: column;
  border: 1.5px solid #cccccc;
}

.text {
  color: #757575;
  text-align: left;
  vertical-align: text-top;
  font-size: 14px;
  line-height: 1.5;
  border-style: hidden;
  outline: none;
  width: 315px;
}
</style>
