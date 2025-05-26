import { useToast } from 'vue-toastification';

const toast = useToast();

export function showErrorToast(message) {
  toast.success(message, {
    timeout: 3000,
    type: 'success',
    position: 'top-center',
    icon: false,
    hideProgressBar: true,
    closeButton: false,
  });
}

export function showSuccessToast(message) {
  toast.success(message, {
    timeout: 3000,
    type: 'error',
    position: 'top-center',
    icon: false,
    hideProgressBar: true,
    closeButton: false,
  });
}

export function showNotificationToast(message) {
  toast.info(message, {
    timeout: 3000,
    type: 'info',
    position: 'top-right',
    icon: false,
    hideProgressBar: true,
    closeButton: false,
  });
}