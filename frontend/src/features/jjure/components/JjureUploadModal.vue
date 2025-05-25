<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';

console.log('Modal 열림');

const props = defineProps({
  videoUrl: { type: String, required: true },
  caption: { type: String, default: '' },
  thumbnail: File,
});

const emit = defineEmits([
  'update:caption',
  'update:videoUrl',
  'update:thumbnail',
  'upload',
  'close',
]);

const localCaption = ref(props.caption);
const localVideoUrl = ref(props.videoUrl);
const localThumbnail = ref(props.thumbnail);

watch(localCaption, (val) => emit('update:caption', val));
watch(localVideoUrl, (val) => emit('update:videoUrl', val));
watch(localThumbnail, (val) => emit('update:thumbnail', val));

watch(
  () => props.caption,
  (val) => (localCaption.value = val),
);
watch(
  () => props.videoUrl,
  (val) => (localVideoUrl.value = val),
);
watch(
  () => props.thumbnail,
  (val) => (localThumbnail.value = val),
);

const thumbnailUrl = ref('');
const thumbnailBlob = ref(null);

// video 엘리먼트 참조
const videoRef = ref(null);

// props 변경 감지 → 내부 URL 반영 + reload
watch(
  () => props.videoUrl,
  async (newVal) => {
    localVideoUrl.value = newVal;
    await nextTick();
    videoRef.value?.load();

    // 썸네일 생성 시도
    await generateThumbnail();
  },
  { immediate: true },
);

// 썸네일 생성
async function generateThumbnail(retryCount = 0) {
  try {
    const base64 = await generateThumbnailFromVideo(localVideoUrl.value);
    thumbnailUrl.value = base64;
    thumbnailBlob.value = dataURItoBlob(base64);
    emit('update:thumbnail', thumbnailBlob.value);
  } catch (err) {
    console.warn(`썸네일 생성 실패 [시도 ${retryCount + 1}/3]:`, err);

    if (retryCount < 5) {
      setTimeout(() => generateThumbnail(retryCount + 1), 1000); // 1초 간격 재시도
    } else {
      console.error('썸네일 최종 생성 실패. 사용자에게는 이전 썸네일이 유지됩니다.');
    }
  }
}

function dataURItoBlob(dataURI) {
  const byteString = atob(dataURI.split(',')[1]);
  const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  return new Blob([ab], { type: mimeString });
}

function generateThumbnailFromVideo(videoUrl) {
  return new Promise((resolve, reject) => {
    const video = document.createElement('video');
    const canvas = document.createElement('canvas');

    video.src = videoUrl;
    video.crossOrigin = 'anonymous';
    video.muted = true;
    video.playsInline = true;

    video.addEventListener('loadeddata', () => {
      if (video.readyState >= 2) {
        video.currentTime = 1;
      } else {
        reject('비디오가 아직 준비되지 않았습니다.');
      }
    });

    video.addEventListener('seeked', () => {
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

      const dataURL = canvas.toDataURL('image/jpeg', 0.8);
      resolve(dataURL);
    });

    video.onerror = () => reject('비디오 로딩 실패');
  });
}

function onThumbnailSelected(e) {
  const file = e.target.files[0];
  if (!file) return;

  thumbnailBlob.value = file;
  const reader = new FileReader();
  reader.onload = () => {
    thumbnailUrl.value = reader.result;
    emit('update:thumbnail', file);
  };
  reader.readAsDataURL(file);
}

function handleSubmit() {
  emit('upload');
}

onMounted(() => {
  generateThumbnail();
});
</script>

<template>
  <section class="overlay" @click.self="emit('close')">
    <article class="reel-modal">
      <div class="video-preview">
        <video ref="videoRef" controls playsinline>
          <source :src="localVideoUrl" type="video/mp4" />
        </video>
      </div>

      <form class="reel-form" @submit.prevent="handleSubmit">
        <div>
          <slot name="video-upload-slot" />
        </div>

        <div class="input-group">
          <div class="modal-header">
            <button class="cancel-button" @click="emit('close')">x</button>
          </div>
          <label for="reel-caption" class="label">쭈르 설명</label>
          <textarea
            id="reel-caption"
            class="textarea"
            placeholder="쭈르 설명을 입력하세요"
            :value="caption"
            @input="emit('update:caption', $event.target.value)"
          ></textarea>
        </div>

        <label class="label mt-4">썸네일 이미지</label>
        <label class="thumbnail-select">
          <img :src="thumbnailUrl" alt="썸네일 미리보기" />
          <input type="file" accept="image/*" @change="onThumbnailSelected" hidden />
        </label>

        <p class="caption-tip">권장 비율: <strong>3:4</strong></p>

        <button type="submit" class="submit-button">쭈르 업로드</button>
      </form>
    </article>
  </section>
</template>

<style scoped>
.overlay {
  @apply fixed inset-0 bg-black-alpha-60 flex justify-center items-center z-50;
}

.reel-modal {
  @apply h-[560px] flex bg-white rounded-lg overflow-hidden shadow-elevated;
  position: relative;
}

.video-preview {
  @apply h-full w-[420px] bg-black flex flex-col items-center justify-center gap-2;
  aspect-ratio: 9 / 16;
}

.video-preview video {
  @apply rounded object-cover;
  aspect-ratio: 3 / 4;
  max-height: 100%;
  max-width: 100%;
}

.reel-form {
  @apply w-[300px] p-6 flex flex-col justify-between;
}

.input-group {
  @apply flex flex-col gap-1 mt-3;
}

.label {
  @apply text-body-sm text-gray-700;
}

.textarea {
  @apply w-full h-[150px] resize-none border border-gray-300 rounded-md p-2 text-body-sm;
}

.caption-tip {
  @apply mt-2 text-[12px] text-gray-500;
}

.submit-button {
  @apply bg-primary text-white text-body-sm py-3 px-4 rounded-md mt-4 hover:bg-primary-hover;
}

.thumbnail-select {
  @apply mt-2 block w-[100px] h-[140px] overflow-hidden rounded border border-gray-300 cursor-pointer;
}
.thumbnail-select img {
  @apply w-full h-full object-cover;
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
