import axios from '@/api/axios';

export const getComments = (targetId, targetType, page = 1, size = 10) => {
  let basePath;

  switch (targetType) {
    case 'FEED':
      basePath = `feeds`;
      break;
    case 'JJURE':
      basePath = `jjure`;
      break;
    default:
      throw new Error(`Unsupported targetType: ${targetType}`);
  }

  return axios.get(`${basePath}/${targetId}/comments`, {
    params: { page, size },
  });
};

export const postComment = (targetId, targetType, content, parentCommentId = null, userId) => {
  let basePath;

  switch (targetType) {
    case 'FEED':
      basePath = `feeds/comments`;
      break;
    case 'JJURE':
      basePath = `jjure/comments`;
      break;
    default:
      throw new Error(`Unsupported targetType: ${targetType}`);
  }

  const payload = {
    targetId,
    targetType,
    content,
    parentCommentId,
  };

  return axios.post(basePath, payload, {
    headers: { 'X-USER-ID': userId },
  });
};

export const deleteComment = (commentId, targetType, userId) => {
  let basePath;

  switch (targetType) {
    case 'FEED':
      basePath = `feeds/comments`;
      break;
    case 'JJURE':
      basePath = `jjure/comments`;
      break;
    default:
      throw new Error(`Unsupported targetType: ${targetType}`);
  }

  return axios.delete(`${basePath}/${commentId}`, {
    headers: { 'X-USER-ID': userId },
  });
};
