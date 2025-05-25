<script setup>
import { nextTick, onMounted, ref } from 'vue';
import draggable from 'vuedraggable';
import { showErrorToast } from '@/utills/toast.js';

const emit = defineEmits(['close', 'fileSelected']);
const inputRef = ref(null);
const images = ref([]); // { file: File, url: string }
const maxImages = 8;
const isDragging = ref(false);
const props = defineProps({
  initialImages: {
    type: Array,
    default: () => [],
  },
});

function openFileDialog() {
  inputRef.value?.click();
}

function handleFileChange(event) {
  const files = Array.from(event.target.files);

  const imageFiles = files.filter((file) => file.type.startsWith('image/'));
  const videoFiles = files.filter((file) => file.type.startsWith('video/'));

  if (videoFiles.length === 1 && imageFiles.length === 0) {
    console.log('[Modal] emit fileSelected', videoFiles);
    emit('fileSelected', { files: videoFiles, existingUrls: [] });
    emit('close');
    return;
  }

  if (imageFiles.length > 0) {
    const newImages = imageFiles.map((file) => ({
      file,
      url: URL.createObjectURL(file),
    }));

    const total = images.value.length + newImages.length;
    if (total > maxImages) {
      alert(`최대 ${maxImages}장까지 업로드할 수 있습니다.`);
      return;
    }

    images.value = [...images.value, ...newImages];
    return;
  }

  showErrorToast('이미지 또는 하나의 비디오만 선택해주세요.');
}

function removeImage(index) {
  URL.revokeObjectURL(images.value[index].url);
  images.value.splice(index, 1);
}

function confirmUpload() {
  if (images.value.length === 0) {
    alert('최소 1장의 이미지를 선택해주세요.');
    return;
  }

  const existingUrls = images.value.filter((i) => i.file === null).map((i) => i.url); // 기존 이미지

  const files = images.value.filter((i) => i.file !== null).map((i) => i.file); // 새로 업로드한 이미지

  emit('fileSelected', { existingUrls, files });
  emit('close');
}

onMounted(() => {
  if (props.initialImages.length) {
    images.value = props.initialImages.map((url) => ({
      file: null,
      url,
    }));
  }
});
</script>

<template>
  <div class="modal-overlay" @click.self="emit('close')">
    <section class="upload-modal">
      <div class="modal-header">
        <button class="cancel-button" @click="emit('close')">x</button>
      </div>
      <div v-if="images.length === 0" class="text-center flex flex-col items-center">
        <img
          src="https://cdn-icons-png.flaticon.com/512/685/685655.png"
          alt="upload"
          class="w-[40px] h-[40px] object-cover mb-[20px]"
        />
        <h2 class="modal-title">사진과 동영상을 여기다 끌어다 놓으세요</h2>
        <p class="modal-description">또는 아래 버튼을 눌러 파일을 선택하세요.</p>
      </div>

      <input
        ref="inputRef"
        type="file"
        accept="image/*,video/*"
        multiple
        class="hidden"
        @change="handleFileChange"
      />

      <p v-if="images.length > 0" class="modal-description">
        최대 {{ maxImages }}장의 사진을 업로드할 수 있습니다.
      </p>
      <draggable
        v-if="images.length > 0"
        v-model="images"
        tag="ul"
        class="image-preview-area"
        item-key="url"
        @start="isDragging = true"
        @end="isDragging = false"
      >
        <template #item="{ element, index }">
          <li class="relative w-[80px] h-[80px] group">
            <img
              :src="element.url"
              class="w-full h-full object-cover rounded block"
              alt="upload-image"
            />
            <button
              v-if="!isDragging"
              class="absolute top-0 right-0 bg-black/50 text-white w-5 h-5 flex justify-center items-center rounded-full text-xs opacity-0 group-hover:opacity-100 transition"
              @click.stop="removeImage(index)"
            >
              ✕
            </button>
          </li>
        </template>
      </draggable>
      <p v-if="images.length > 0" class="modal-description">고양이 사진만 업로드 가능합니다.</p>

      <div class="flex w-full gap-8 mt-4 justify-center">
        <button class="modal-button" @click="openFileDialog">컴퓨터에서 선택</button>
        <button v-if="images.length > 0" class="modal-button" @click="confirmUpload">업로드</button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.modal-overlay {
  @apply fixed inset-0 bg-black-alpha-60 flex justify-center items-center z-50;
}

.upload-modal {
  @apply w-[480px] min-h-[600px] bg-white rounded-lg px-10 py-10 text-center shadow-elevated flex flex-col justify-center items-center;
  position: relative;
}

.modal-title {
  @apply text-body-lg font-semibold mb-5;
}

.modal-description {
  @apply text-body-sm text-gray-500 mb-5;
}

.modal-button {
  @apply bg-primary text-white text-body-sm px-4 py-2 rounded-md hover:bg-primary-hover;
}

.image-preview-area {
  @apply w-full max-w-md
  grid grid-cols-4 gap-2
  my-4 h-72
  border-2 border-dashed border-gray-300
  rounded-lg p-4 overflow-auto
  place-items-start;
}

.modal-header {
  @apply w-full flex justify-end;
  position: absolute;
  top: 16px;
  right: 16px;
}

.cancel-button {
  @apply bg-primary text-white text-body-sm py-1 px-3 rounded-sm hover:bg-primary-hover;
}
</style>
