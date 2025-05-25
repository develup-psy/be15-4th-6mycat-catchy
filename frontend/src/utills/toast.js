import { useToast } from 'vue-toastification';

const toast = useToast();

export function showErrorToast(message) {
  toast.success(message, {
    timeout: 3000,
    position: 'top-center',
    icon: false,
    hideProgressBar: true,
    closeButton: false,
  });
}

export function showSuccessToast(message) {
  toast.success(message, {
    timeout: 3000,
    position: 'top-center',
    icon: false,
    hideProgressBar: true,
    closeButton: false,
  });
}
