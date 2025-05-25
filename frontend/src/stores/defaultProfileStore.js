import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 고정 기본 이미지
import defaultImage from '@/assets/default_images/04_cat.png';

export const useDefaultProfileStore = defineStore('defaultProfile', () => {
  const image = ref(null); // 회원가입 시 선택한 이미지
  const nickname = ref('');

  function setProfileImage(url) {
    image.value = url ?? defaultImage;
  }

  function setNickname(name) {
    nickname.value = name;
  }

  function clearProfileImage() {
    image.value = null;
  }

  const hasImage = computed(() => !!image.value);

  return {
    image,
    nickname,
    hasImage,
    defaultImage,
    setProfileImage,
    setNickname,
    clearProfileImage,
  };
});
