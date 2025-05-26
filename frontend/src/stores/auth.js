import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { logout as logoutApi, deleteMember as deleteMemberApi } from '@/api/member.js';
import { useDefaultProfileStore } from '@/stores/defaultProfileStore.js';
import { showErrorToast } from '@/utills/toast.js';
import { closeNotificationConnection, subscribeToNotification } from '@/api/notificationSse.js';
import { useNotificationStore } from '@/stores/notification.js';

export const useAuthStore = defineStore(
  'auth',
  () => {
    const accessToken = ref(null);
    const expirationTime = ref(null);
    const memberId = ref(null);

    const isAuthenticated = computed(
      () => !!accessToken.value && Date.now() < (expirationTime.value || 0),
    );

    function setAuth(at) {

      accessToken.value = at;
      try {
        const payload = JSON.parse(atob(at.split('.')[1]));
        if (!payload.exp) throw new Error('만료 시간 없음');
        expirationTime.value = payload.exp * 1000;
        memberId.value = payload.sub;

        // SSE 알림 구독
        const notificationStore = useNotificationStore();

        subscribeToNotification((data) => {
          notificationStore.prependNotification(data);
        });

      } catch (e) {
        accessToken.value = null;
        expirationTime.value = null;
        memberId.value = null;
      }
    }

    function clearAuth() {
      accessToken.value = null;
      expirationTime.value = null;
      memberId.value = null;
    }

    async function logout() {
      try {
        closeNotificationConnection();
        await logoutApi();
      } catch (err) {
        showErrorToast('로그아웃이 실패했습니다. 다시 시도해주세요.');
      } finally {
        clearAuth();
        useDefaultProfileStore().clearProfileImage();
      }
    }

    async function deleteMember() {
      try {
        await logoutApi(); // 먼저 로그아웃 처리
        await deleteMemberApi(); // 회원 탈퇴 요청
      } catch (err) {
        showErrorToast('회원 탈퇴에 실패했습니다. 다시 시도해주세요.');
        throw err; // 필요하면 상위 컴포넌트에서 catch
      } finally {
        clearAuth();
      }
    }

    return {
      accessToken,
      expirationTime,
      memberId,
      isAuthenticated,
      setAuth,
      clearAuth,
      logout,
      deleteMember,
    };
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['accessToken', 'expirationTime', 'memberId'],
    },
  },
);
