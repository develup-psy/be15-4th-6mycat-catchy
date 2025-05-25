<script setup>
import { ref, onMounted, computed } from 'vue';
import { fetchMyProfile } from '@/api/member.js';
import MemberInfoItem from '@/features/member/components/MemberInfoItem.vue';
import RedButton from '@/features/member/components/RedButton.vue';
import DeleteMemModal from '@/features/member/components/DeleteMemModal.vue';
import { startLoading, stopLoading } from '@/composable/useLoadingBar.js';

const name = ref('');
const contactNumber = ref('');
const email = ref('');
const social = ref('');
const isModalOpen = ref(false);

const SOCIAL_LABELS = {
  KAKAO: '카카오',
  NAVER: '네이버',
  GOOGLE: '구글',
};

const socialLabel = computed(() => SOCIAL_LABELS[social.value] || social.value);

const formattedContactNumber = computed(() =>
  contactNumber.value ? contactNumber.value.replace(/^(\d{3})(\d{4})(\d{4})$/, '$1-$2-$3') : '',
);

onMounted(async () => {
  try {
    startLoading();
    const res = await fetchMyProfile();
    const data = res.data.data;
    name.value = data.name;
    contactNumber.value = data.contactNumber;
    email.value = data.email;
    social.value = data.social;
  } catch (err) {
    console.error('프로필 정보 가져오기 실패:', err);
  } finally {
    stopLoading();
  }
});
</script>

<template>
  <div class="background">
    <div class="header-frame">
      <span class="header-text">회원 정보</span>
    </div>
    <MemberInfoItem title="이름" :content="name" />
    <MemberInfoItem title="전화번호" :content="formattedContactNumber" />
    <MemberInfoItem title="이메일" :content="email" />
    <MemberInfoItem title="소셜 로그인" :content="socialLabel" />

    <RedButton @click="isModalOpen = true">탈퇴하기</RedButton>
    <DeleteMemModal :visible="isModalOpen" @close="isModalOpen = false" />
  </div>
</template>

<style scoped>
.background {
  background-color: #ffffff;
  height: auto;
  width: 565px;
  filter: drop-shadow(0px 4px 12px rgba(0, 0, 0, 0.04));
  border-radius: 12px;
  padding: 25px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.header-frame {
  height: 31px;
  width: auto;
  display: flex;
  flex-direction: column;
}

.header-text {
  color: #1e1e1e;
  text-align: left;
  font-size: 24px;
  line-height: 1.5;
  font-weight: bold;
  width: auto;
}
</style>
