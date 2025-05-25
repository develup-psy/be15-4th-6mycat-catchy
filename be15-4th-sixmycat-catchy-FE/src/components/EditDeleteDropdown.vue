<template>
  <div class="relative" ref="dropdownRef">
    <button @click.stop="toggle" class="text-xs text-black">
      <i class="fa-solid fa-ellipsis"></i>
    </button>

    <div v-if="open" class="dropdown-menu">
      <button class="block w-full text-left px-4 py-3 hover:bg-gray-100" @click="handleEdit">
        수정
      </button>
      <button
        class="block w-full text-left px-4 py-3 text-red-500 hover:bg-gray-100"
        @click="handleDelete"
      >
        삭제
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const emit = defineEmits(['edit', 'delete']);
const open = ref(false);
const dropdownRef = ref(null);

const toggle = () => {
  open.value = !open.value;
};

const handleEdit = () => {
  emit('edit');
  open.value = false;
};

const handleDelete = () => {
  emit('delete');
  open.value = false;
};

// 외부 클릭 시 닫기
const handleClickOutside = (e) => {
  if (dropdownRef.value && !dropdownRef.value.contains(e.target)) {
    open.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
.dropdown-menu {
  @apply absolute right-0 mt-2 w-24 bg-white border rounded shadow z-10 text-xs;
}
</style>
