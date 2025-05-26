// src/utils/notification.js
import { showNotificationToast } from '@/utills/toast.js';
import { useAuthStore } from '@/stores/auth.js';
import { EventSourcePolyfill } from 'event-source-polyfill';

let eventSource = null;

export function subscribeToNotification(onMessageCallback) {
  if (eventSource) {
    eventSource.close();
  }

  const authStore = useAuthStore();
  const accessToken = authStore.accessToken;
  const lastEventId = localStorage.getItem('lastEventId') || '';
  const baseUrl = import.meta.env.VITE_API_URL;

  eventSource = new EventSourcePolyfill(`${baseUrl}/notifications/connect`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
      'Last-Event-Id': lastEventId,
    },
    withCredentials: true,
  });

  eventSource.onopen = () => {
    console.log('✅ SSE 연결됨');
  };

  eventSource.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      const id = event.lastEventId || event.id;
      if (id) localStorage.setItem('lastEventId', id);

      console.log('📩 알림 수신:', data);
      onMessageCallback(data);
      // 여기에 toast, 알림 UI, store 업데이트 등 연결
      console.log(data);
      showNotificationToast(data.senderNickname + '님이 ' + data.content);
    } catch (err) {
      console.error('❌ 알림 파싱 실패:', err);
    }
  };

  eventSource.onerror = (err) => {
    console.error('❌ SSE 연결 오류:', err);
  };
}

export function closeNotificationConnection() {
  if (eventSource) {
    eventSource.close();
    console.log('🔌 SSE 연결 종료');
  }
}
