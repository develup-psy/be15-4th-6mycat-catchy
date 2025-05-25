import api from '@/api/axios'; // 공통 axios 인스턴스 사용

// 차단 목록 조회
export const fetchBlockedUsers = (userId, page = 1, size = 10) => {
  return api.get('/blocks', {
    headers: {
      'X-USER-ID': userId,
    },
    params: {
      page,
      size,
    },
  });
};

// 차단 등록
export const blockUser = (userId, blockedId) => {
  return api.post(`/blocks/${blockedId}`, null, {
    headers: {
      'X-USER-ID': userId,
    },
  });
};

// 차단 해제
export const unblockUser = (userId, blockedId) => {
  return api.delete(`/blocks/${blockedId}`, {
    headers: {
      'X-USER-ID': userId,
    },
  });
};
