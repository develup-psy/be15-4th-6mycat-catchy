<script setup>
import { ref, computed } from 'vue';

const emit = defineEmits(['close', 'imagesSelected']);
const imageFiles = ref([]); // { file: File, url: string }[]

function handleFileChange(event) {
  const files = [...event.target.files];
  const validFiles = files.slice(0, 8 - imageFiles.value.length);

  validFiles.forEach((file) => {
    const url = URL.createObjectURL(file);
    imageFiles.value.push({ file, url });
  });

  event.target.value = '';
}

function removeImage(index) {
  const removed = imageFiles.value.splice(index, 1);
  if (removed[0]?.url) URL.revokeObjectURL(removed[0].url);
}

function confirmSelection() {
  const files = imageFiles.value.map((item) => item.file);
  emit('imagesSelected', files);
  emit('close');
}

// 항상 8칸으로 맞춘 배열 (빈 칸은 null)
const paddedImages = computed(() => {
  const padded = [...imageFiles.value];
  while (padded.length < 8) padded.push(null);
  return padded;
});
</script>

<template>
  <div class="modal-backdrop">
    <section class="modal-container" role="dialog" aria-modal="true">
      <h1 class="modal-title">카드 이미지 선택 (최대 8장)</h1>
      <p class="modal-subtitle">원하는 고양이 이미지를 최대 8장까지 선택해주세요.</p>

      <div class="file-upload">
        <label for="imageUpload" class="file-label">이미지 선택하기</label>
        <input type="file" id="imageUpload" accept="image/*" multiple @change="handleFileChange" />
      </div>

      <div class="preview-grid">
        <div v-for="(item, index) in paddedImages" :key="index" class="preview-item relative">
          <template v-if="item">
            <img :src="item.url" :alt="`preview-${index + 1}`" />
            <button class="remove-button" @click="removeImage(index)" aria-label="삭제">×</button>
          </template>
          <template v-else>
            <div class="placeholder" />
          </template>
        </div>
      </div>

      <div class="note">※ 8장 미만 선택 시, 기본 이미지가 자동으로 포함됩니다.</div>

      <div class="confirm-buttons">
        <button class="confirm" @click="confirmSelection">확인</button>
        <button class="cancel" @click="$emit('close')">닫기</button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.modal-backdrop {
  @apply fixed inset-0 bg-black bg-opacity-20 backdrop-blur-sm flex items-center justify-center z-50;
}

.modal-container {
  @apply bg-white border border-pink-200 p-10 rounded-xl shadow-md max-w-3xl w-[90%] text-center;
}

.modal-title {
  @apply text-2xl font-bold text-pink-500 mb-3;
}

.modal-subtitle {
  @apply text-sm text-gray-600 mb-6;
}

.file-upload {
  @apply flex justify-center mb-5;
}

.file-label {
  @apply bg-pink-500 text-white px-5 py-2 rounded-lg text-sm font-semibold cursor-pointer hover:bg-pink-600;
}

input[type='file'] {
  display: none;
}

.preview-grid {
  @apply grid grid-cols-4 gap-3 mt-5;
}

.preview-item {
  @apply relative w-full pt-[100%] bg-white rounded-lg overflow-hidden border border-gray-200 shadow-sm;
}

.preview-item img {
  @apply absolute top-0 left-0 w-full h-full object-cover;
}

.placeholder {
  @apply absolute top-0 left-0 w-full h-full bg-gray-100;
}

.remove-button {
  @apply absolute top-1 right-1 bg-black bg-opacity-50 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs hover:bg-opacity-80;
}

.note {
  @apply mt-5 text-sm text-yellow-500;
}

.confirm-buttons {
  @apply mt-6 flex justify-center gap-4;
}

.confirm {
  @apply bg-pink-500 text-white px-5 py-2 rounded-md font-semibold hover:bg-pink-600;
}

.cancel {
  @apply bg-gray-200 text-gray-700 px-5 py-2 rounded-md font-semibold hover:bg-gray-300;
}
</style>
