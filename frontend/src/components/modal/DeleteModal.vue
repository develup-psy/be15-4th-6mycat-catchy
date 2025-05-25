<script setup>
const emit = defineEmits(['close', 'confirm', 'delete']);
const close = () => emit('close');
const handleDelete = () => emit('delete');
</script>

<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal-content">
      <h2 class="text-lg font-bold mb-4 text-error">정말 삭제하시겠어요?</h2>
      <div class="flex justify-end gap-2">
        <button @click="close" class="text-sm px-4 py-2 bg-gray-200 rounded">취소</button>
        <button @click="handleDelete" class="text-sm px-4 py-2 bg-error text-white rounded">
          삭제
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  @apply fixed inset-0 bg-black-alpha-40 flex items-center justify-center z-50;
}
.modal-content {
  @apply bg-white rounded-lg shadow-elevated p-6 w-[300px];
}
</style>
