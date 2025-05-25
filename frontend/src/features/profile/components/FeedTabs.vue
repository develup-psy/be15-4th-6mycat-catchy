<script setup>
import { computed } from 'vue';

const { selectedTab, isOther } = defineProps({
  selectedTab: {
    type: String,
    required: true,
  },
  isOther: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:selectedTab']);

const tabMap = [
  { label: '피드', value: 'MyFeed' },
  { label: '쭈르', value: 'MyJjure' },
  { label: '피드 좋아요', value: 'LikedFeed' },
  { label: '쭈르 좋아요', value: 'LikedJjure' },
];

const otherTabMap = [
  { label: '피드', value: 'OtherFeed' },
  { label: '쭈르', value: 'OtherJjure' },
];

const tabsToRender = computed(() => (isOther ? otherTabMap : tabMap));

const changeTab = (value) => {
  emit('update:selectedTab', value);
};
</script>

<template>
  <div class="flex justify-center mt-6 mb-4">
    <div class="flex gap-6 pl-2 pb-2 border-b border-gray-200 w-[400px] font-sans text-body-md">
      <button
        v-for="tab in tabsToRender"
        :key="tab.value"
        @click="changeTab(tab.value)"
        :class="[
          'transition-colors duration-200',
          selectedTab === tab.value
            ? 'text-primary border-b-2 border-primary font-semibold'
            : 'text-gray-400',
        ]"
      >
        {{ tab.label }}
      </button>
    </div>
  </div>
</template>
