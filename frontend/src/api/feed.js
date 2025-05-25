import api from './axios.js';

export const uploadImages = (data) => api.post('/feed/images', data);

export const createFeed = (data) => api.post('/feeds', data);

export const fetchFeedList = (page = 0, size = 5) => {
  return api.get('/feeds', {
    params: {
      page: page - 1,
      size,
    },
  });
};

export const fetchFeed = (feedId) => api.get(`/feeds/${feedId}`);

export const editFeed = (feedId, data) => api.put(`/feeds/${feedId}`, data);

export const deleteFeed = (feedId) => api.delete(`/feeds/${feedId}`);

/* 내가 등록한 피드 썸네일 목록 조회 */
export const fetchMyFeedList = (params = { page: 0, size: 9 }) => {
  params.page = params.page - 1;
  return api.get('/feeds/me', {
    params,
  });
};

/* 내가 좋아요한 피드 썸네일 목록 조회 */
export const fetchLikedFeedList = (params = { page: 0, size: 9 }) => {
  params.page = params.page - 1;
  return api.get('/feeds/likes', {
    params,
  });
};

/* 남이 등록한 피드 썸네일 목록 조회 */
export const fetchOtherFeedList = (memberId, params = { page: 0, size: 9 }) => {
  params.page = params.page - 1;
  return api.get(`/feeds/member/${memberId}`, {
    params,
    headers: {
      'X-USER-ID': 1,
    },
  });
};
