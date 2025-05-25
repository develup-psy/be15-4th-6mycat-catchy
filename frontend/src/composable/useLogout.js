import { useAuthStore } from '@/stores/auth';

export async function logoutAndRedirect(router, redirectPath = '/feed') {
  const authStore = useAuthStore();
  await authStore.logout();
  router.push(redirectPath);
}
