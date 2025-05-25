<script setup>
import BasicModal from '@/components/modal/BasicModal.vue';
import RedButton from '@/features/member/components/RedButton.vue';
import { deleteMember } from '@/api/member.js';
import { useAuthStore } from '@/stores/auth.js';
import { useRouter } from 'vue-router';
import { showErrorToast, showSuccessToast } from '@/utills/toast.js';

defineProps({ visible: Boolean });
const emit = defineEmits(['close']);

const router = useRouter();
const authStore = useAuthStore();

async function handleDelete() {
  try {
    // 회원 탈퇴
    console.log('탈퇴');
    await deleteMember();
    authStore.deleteMember(); // Pinia 상태 초기화

    showSuccessToast('그동안 Catchy를 이용해주셔서 감사합니다.');
    router.push('/feed');
  } catch (err) {
    showErrorToast('탈퇴에 실패했습니다. 다시 시도해주세요.');
  }
}
</script>

<template>
  <Teleport to="body">
    <BasicModal v-if="visible" @close="emit('close')">
      <div class="content">
        <p>탈퇴를 하면 다시는 기록을 복구할 수 없습니다.<br />그래도 탈퇴하시겠습니까?</p>
        <div class="actions">
          <RedButton @click="handleDelete">탈퇴하기</RedButton>
        </div>
      </div>
    </BasicModal>
  </Teleport>
</template>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 16px;
}

.actions {
  display: flex;
  gap: 12px;
}
</style>
