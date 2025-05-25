import api from './axios.js';

export async function likeFeed(request) {
  await api.post('/likes', request);
}

export async function unLikeFeed(request) {
  await api.delete('/likes', {
    data: request,
  });
}

// export const likeFeed = (request) => api.post('/likes', request);

// export const unLikeFeed = (request) =>
//   api.delete('/likes', {
//     data: request,
//   });
