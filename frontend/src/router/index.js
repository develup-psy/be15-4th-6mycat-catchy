import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth.js';
import AppShell from '@/components/AppShell.vue';
import { JjureRoutes } from '@/features/jjure/router.js';
import gameRoutes from '@/features/game/router.js';
import { FeedRoutes } from '@/features/feed/router.js';
import { MemberRoutes } from '@/features/member/router.js';
import { showErrorToast } from '@/utills/toast.js';
import blockRoutes from '@/features/block/router';
import profileRoutes from '@/features/profile/router';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/feed',
      component: AppShell,
      children: [
        ...JjureRoutes,
        ...gameRoutes,
        ...FeedRoutes,
        ...profileRoutes,
        ...MemberRoutes,
        ...blockRoutes,
      ],
    },
  ],
});

// 전역 가드
router.beforeEach((to) => {
  const authStore = useAuthStore();

  // 인증이 필요한데 로그인 안 된 경우 → 알림 띄움
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    showErrorToast('로그인이 필요한 페이지입니다.');

    // 1초 후 /feed 로 이동
    setTimeout(() => {
      router.push('/feed');
    }, 1000);
  }

  /* 로그인 되어 있는데 Login 및 signup으로 이동시 메인으로 이동 */
  if ((to.name === 'login' || to.name === 'signup') && authStore.isAuthenticated) {
    return { name: 'main' };
  }

  // 로그인된 사용자가 로그인/회원가입 페이지에 접근하면 메인으로
  // 로그인된 사용자가 비회원 전용 페이지 접근 시
  if (to.meta.guestOnly && authStore.isAuthenticated) {
    showErrorToast('이미 로그인된 상태입니다.');

    return { path: '/feed' };
  }
});

export default router;
