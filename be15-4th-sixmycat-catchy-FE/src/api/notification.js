import api from './axios.js';

export function getNotifications(page = 1, size = 10) {
  return api.get('/notifications/me', {
    params: {
      page,
      size,
    },
  });
}
