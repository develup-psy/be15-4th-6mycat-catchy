<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import BasicButton from '@/features/member/components/BasicButton.vue';
import Input from '@/features/member/components/Input.vue';
import CatFormModal from '@/features/profile/components/CatFormModal.vue';
import { getTempMemberInfo, socialSignupExtra } from '@/api/member';
import { addNewCat } from '@/api/profile';
import { useDefaultProfileStore } from '@/stores/defaultProfileStore';
import { useAuthStore } from '@/stores/auth';
import { useToast } from 'vue-toastification';
import { showErrorToast, showSuccessToast } from '@/utills/toast.js';
import { startLoading, stopLoading } from '@/composable/useLoadingBar.js';

const router = useRouter();
const defaultProfileStore = useDefaultProfileStore();
const authStore = useAuthStore();
const toast = useToast();


const name = ref('');
const contactNumber = ref('');
const nickname = ref('');
const profileImage = ref(null);

const cats = ref([]);
const showCatModal = ref(false);
const editIndex = ref(null);
const currentCat = ref({}); // ✅ 모달에 넘길 현재 고양이 데이터

const nameReadonly = ref(false);
const contactReadonly = ref(false);

const fileInput = ref(null);

const urlParams = new URLSearchParams(window.location.search);
const email = urlParams.get('email');
const social = urlParams.get('social');

const previewImage = computed(() =>
  profileImage.value ? URL.createObjectURL(profileImage.value) : defaultProfileStore.defaultImage,
);

const triggerImageInput = () => {
  fileInput.value?.click();
};

const handleImageChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    profileImage.value = file;
  }
};

onMounted(async () => {
  if (!defaultProfileStore.hasImage) {
    defaultProfileStore.setProfileImage();
  }

  if (!email || !social) {
    showErrorToast('잘못된 접근입니다. 이메일 또는 소셜 정보가 누락되었습니다.');
    return;
  }

  startLoading();
  try {
    const res = await getTempMemberInfo(email, social.toUpperCase());
    const data = res.data.data;
    if (data.name) {
      name.value = data.name;
      nameReadonly.value = true;
    }
    if (data.contactNumber) {
      contactNumber.value = data.contactNumber.replace(/-/g, '');
      contactReadonly.value = true;
    }
  } catch (err) {
    showErrorToast('회원 정보를 불러오지 못했습니다. 다시 시도해주세요.');
    router.push('/member/start');
  } finally {
    stopLoading();
  }
});

const submitSignup = async () => {
  try {
    startLoading();

    const formData = new FormData();
    formData.append('name', name.value);
    formData.append('contactNumber', contactNumber.value.replace(/-/g, ''));
    formData.append('nickname', nickname.value);
    formData.append('email', email);
    formData.append('social', social.toUpperCase());

    if (profileImage.value) {
      formData.append('profileImage', profileImage.value);
    }

    const { data } = await socialSignupExtra(formData);
    const accessToken = data.data.accessToken;

    authStore.setAuth(accessToken);

    for (const cat of cats.value) {
      await addNewCat(cat);
    }

    showSuccessToast('Catchy에 오신 것을 환영합니다!');
    router.push('/feed');
  } catch (error) {
    const { message } = error.response?.data ?? {};
    showErrorToast(`${message ?? '알 수 없는 오류가 발생했습니다.'}`);
  } finally {
    stopLoading();
  }
};

const handleContactInput = (e) => {
  const raw = e.target.value.replace(/\D/g, '');
  contactNumber.value = raw.slice(0, 11);
};

const handleNicknameInput = (e) => {
  nickname.value = e.target.value.slice(0, 20);
};

function openAddCat() {
  editIndex.value = null;
  currentCat.value = null;
  showCatModal.value = true;
}

function openEditCat(index) {
  editIndex.value = index;
  currentCat.value = { ...cats.value[index] }; // ✅ 기존 값 복사
  showCatModal.value = true;
}

function handleAddCat(cat) {
  if (editIndex.value !== null) {
    cats.value[editIndex.value] = cat;
    editIndex.value = null;
  } else {
    cats.value.push(cat);
  }
  showCatModal.value = false;
}

function handleDeleteCat(cat) {
  cats.value = cats.value.filter((c) => c !== cat);
}
</script>

<template>
  <div class="main-frame">
    <div class="signup-button-frame">
      <BasicButton
        text="회원가입 하기"
        backgroundColor="#ff5c8d"
        textColor="#ffffff"
        @click="submitSignup"
      />
    </div>

    <div class="profile-frame">
      <img
        :src="previewImage"
        alt="프로필 이미지"
        class="profile-image"
        @click="triggerImageInput"
      />
      <img
        src="@/assets/images/member/camera-pink.png"
        class="camera-icon"
        @click="triggerImageInput"
        alt="image add"
      />
      <input
        type="file"
        ref="fileInput"
        accept="image/*"
        class="file-input-hidden"
        @change="handleImageChange"
      />
    </div>

    <h2 class="title">추가 정보 입력</h2>

    <div class="inputs-frame">
      <Input
        title="이름"
        text="이름을 입력해주세요 (후에 수정 불가)"
        v-model="name"
        :readonly="nameReadonly"
      />
      <Input
        title="전화번호"
        text="- 없이 전화번호를 입력해주세요 (후에 수정 불가)"
        v-model="contactNumber"
        :readonly="contactReadonly"
        @input="handleContactInput"
      />
      <Input
        title="닉네임"
        text="영어, 숫자, 특수문자 (., _, ^)만 가능합니다"
        v-model="nickname"
        @input="handleNicknameInput"
      />

      <!-- ✅ 고양이 정보 입력 영역 -->
      <label class="input-title">고양이 정보</label>
      <p v-if="cats.length === 0" class="input-desc">
        아직 등록된 고양이가 없습니다.
      </p>

      <div
        v-for="(cat, index) in cats"
        :key="index"
        @click="openEditCat(index)"
        class="input-box cat-box"
      >
        {{ cat.name }} · {{ cat.breed }} · {{ cat.gender }}
      </div>

      <button class="input-box cat-add-btn" @click="openAddCat">
        고양이 추가
      </button>
    </div>

    <!-- 고양이 입력 모달 -->
    <CatFormModal
      :visible="showCatModal"
      @close="showCatModal = false"
      @submit="handleAddCat"
      @delete="handleDeleteCat"
      :initial-cat="currentCat"
    />
  </div>
</template>

<style scoped>
.main-frame {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding-top: 80px;
  padding-bottom: 80px;
}

.profile-frame {
  position: relative;
  width: 100px;
  height: 100px;
}

.profile-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
  border: 1px solid #ddd;
  cursor: pointer;
}

.camera-icon {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 28px;
  height: 28px;
  cursor: pointer;
}

.file-input-hidden {
  display: none;
}

.inputs-frame {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 300px;
}

.signup-button-frame {
  display: flex;
  justify-content: flex-end;
  width: 370px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.input-title {
  font-size: 14px;
  color: #757575;
  margin-top: 12px;
  line-height: 1.5;
  border-style: hidden;
  width: 315px;
}

.input-desc {
  font-size: 14px;
  color: #757575;
  margin-top: 12px;
}

.input-box {
  font-size: 14px;
  border: 1px solid #cccccc;
  border-radius: 6px;
  padding: 10px 12px;
  background-color: #fff;
  transition: all 0.2s ease;
  width: 337px;
  text-align: left;
}

.cat-box {
  cursor: pointer;
  margin-bottom: 8px;
}
.cat-box:hover {
  background-color: #ffe5ec;
  color: #ff5c8d;
}

.cat-add-btn {
  color: #ff5c8d;
  font-weight: 500;
  text-align: center;
  border: 1px solid #ff5c8d;
}
.cat-add-btn:hover {
  background-color: #ffe5ec;
  color: white;
}
</style>
