// src/utils/notification.js
let eventSource = null;

export function subscribeToNotification(onMessageCallback) {
  if (eventSource) {
    eventSource.close(); // 기존 연결 닫기
  }

  eventSource = new EventSource('/api/v1/notifiaction/connect', { withCredentials: true });

  eventSource.onopen = () => {
    console.log('✅ SSE 연결됨');
  };

  eventSource.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      console.log('📩 알림 수신:', data);
      onMessageCallback(data); // 알림 처리 콜백
    } catch (err) {
      console.error('❌ 알림 파싱 실패:', err);
    }
  };

  eventSource.onerror = (err) => {
    console.error('❌ SSE 연결 오류:', err);
    // 연결이 끊어지면 자동으로 재연결 시도됨 (기본 동작)
  };
}

export function closeNotificationConnection() {
  if (eventSource) {
    eventSource.close();
    console.log('🔌 SSE 연결 종료');
  }
}
