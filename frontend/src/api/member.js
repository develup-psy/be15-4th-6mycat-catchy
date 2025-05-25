/* user 관련 api 호출 */
import api from './axios.js';

/* 로그인 */
export function loginUserTest(data) {
  return api.post('/members/login/test', data);
}

// 소셜 회원가입 추가 정보 입력 후 회원가입 완료
export function socialSignupExtra(formData) {
  return api.post('/members/signup/extra', formData);
}

// 임시 회원 정보 조회
export function getTempMemberInfo(email, social) {
  return api.get('/members/temp-info', {
    params: {
      email,
      social,
    },
  });
}

export async function fetchMyProfile() {
  return api.get('/members/me');
}

export async function logout(accessToken) {
  return api.post('/members/logout', null, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
    withCredentials: true,
  });
}

export function reissueAccessToken() {
  return api.get('/members/token');
}

export function deleteMember() {
  return api.delete('/members');
}
