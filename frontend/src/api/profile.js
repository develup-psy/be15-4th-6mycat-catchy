import axios from './axios';

export async function fetchMyProfile() {
  return axios.get('/profiles/me').then((res) => res.data);
}

export async function fetchUserProfile(id) {
  return axios.get(`/profiles/${id}`).then((res) => res.data);
}

export async function updateMyProfile(payload) {
  return axios.patch('/profiles/me', payload).then((res) => res.data);
}

export async function addNewCat(cat) {
  return axios.post('/profiles/cats', cat);
}

export async function deleteCat(catId) {
  return axios.delete(`/profiles/cats/${catId}`);
}
