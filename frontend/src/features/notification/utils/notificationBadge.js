// src/utils/notificationBadge.js

import { ref } from 'vue';

// 외부에서 접근 가능한 반응형 상태
export const hasUnreadNotification = ref(false);

// 알림이 왔을 때 호출
export function showNotificationBadge() {
  console.log('뱃지 보여주기')
  hasUnreadNotification.value = true;
}

// 알림을 확인했을 때 호출
export function clearNotificationBadge() {
  hasUnreadNotification.value = false;
}
