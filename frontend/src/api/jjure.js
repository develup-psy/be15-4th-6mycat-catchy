// 📁 src/api/upload.js
import api from '@/api/axios.js';
import axios from 'axios'; // S3에 직접 업로드할 때는 별도로 사용

/* 1. Presigned URL 요청 */
export async function getPresignedUrl(filename, contentType) {
  const encodedFilename = encodeURIComponent(filename);
  const encodedContentType = encodeURIComponent(contentType);

  const res = await api.get(
    `/jjure/upload/presigned-url?filename=${encodedFilename}&contentType=${encodedContentType}`,
  );
  return res.data.data;
}

/* 2. S3로 파일 업로드 (Presigned URL 사용) */
export async function uploadFileToS3(presignedUrl, file) {
  await axios.put(presignedUrl, file, {
    headers: {
      'Content-Type': file.type,
      'x-amz-acl': 'private',
    },
  });
}

/* 3. 서버에 업로드된 파일 메타정보 저장 */
export async function saveJjureMeta(payload) {
  await api.post('/jjure/upload', payload);
}

/* 4. 쭈르 목록 조회 */
export function fetchJjureList({ page = 0, size = 10 }) {
  return api.get('/jjures', {
    params: { page, size },
  });
}

/* 5. 쭈르 상세 조회 */
export function fetchJjureDetail(jjureId) {
  return api.get(`/jjures/${jjureId}`);
}

/* 6. 썸네일 이미지 등록 (CloudFront URL 반환) */
export function uploadThumbnailImage(file) {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/jjure/upload/thumbnail', formData);
}

/* 7. 쭈르 삭제 */
export async function deleteJjure(jjureId) {
  return await api.delete(`/jjure/upload/${jjureId}`);
}

/* 9. 쭈르 수정 */
export async function updateJjure(jjureId, payload) {
  return await api.put(`/jjure/upload/${jjureId}`, payload);
}

/* 10. 댓글 목록 조회 */
export function fetchJjureComments(jjureId, { page = 0, size = 10 }) {
  return api.get(`/jjure/${jjureId}/comments`, {
    params: { page, size },
  });
}

/* 11. 댓글 등록 */
export function postJjureComment({ jjureId, content }) {
  return api.post('/jjure/comments', {
    jjureId,
    content,
  });
}

/* 12. 댓글 삭제 */
export function deleteJjureComment(commentId) {
  return api.delete(`/jjure/comments/${commentId}`);
}

/* 7. 내가 등록한 쭈르 썸네일 목록 조회 */
export function fetchMyJjureList(params = { page: 0, size: 9 }) {
  params.page = params.page - 1;
  return api.get('/jjures/me', {
    params,
  });
}

/* 8. 내가 좋아요한 쭈르 썸네일 목록 조회 */
export function fetchLikedJjureList(params = { page: 0, size: 9 }) {
  params.page = params.page - 1;
  return api.get('/jjures/likes', {
    params,
  });
}

/* 남이 등록한 쭈르 썸네일 목록 조회 */
export function fetchOtherJjureList(memberId, params = { page: 0, size: 9 }) {
  params.page = params.page - 1;
  return api.get(`/jjures/member/${memberId}`, {
    params,
  });
}
