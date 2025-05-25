// src/composables/useLoadingBar.js
import { ref } from 'vue';

export const isLoading = ref(false);
export const isLoadingAsync = ref(false);

export function startLoading() {
  isLoading.value = true;
}

export function startLoadingAsync() {
  isLoadingAsync.value = true;
}

export function stopLoadingAsync() {
  isLoadingAsync.value = false;
}

export function stopLoading() {
  isLoading.value = false;
}
