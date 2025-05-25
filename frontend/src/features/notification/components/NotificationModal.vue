<script setup>
import NotificationList from '@/features/notification/components/NotificationList.vue';
import { ref, toRef, watch } from 'vue';
import { getNotifications } from '@/api/notification.js';
import { startLoading } from '@/composable/useLoadingBar.js';
import { useInfiniteScroll } from '@/composable/useInfiniteScroll.js';

const props = defineProps({
  isModalOpen: {
    type: Boolean,
    required: true,
  },
});

const isModalOpenRef = toRef(props, 'isModalOpen');

const emit = defineEmits(['close']);

const scrollContainer = ref(null);

const fetchFn = async (page) => {
  try {
    startLoading();
    const { data } = await getNotifications(page);
    return data;
  } catch (e) {
    console.log(e + '알림 목록 로드 실패');
  }
};

const {
  items: notifications,
  isLastPage,
  reset,
} = useInfiniteScroll({
  fetchFn,
  scrollTargetRef: scrollContainer,
});

watch(isModalOpenRef, (newVal, oldVal) => {
  if (newVal !== oldVal && newVal === true) {
    console.log('리셋완료!');
    reset();
  }
});
</script>

<template>
  <div class="modal-overlay" @click.self="emit('close')">
    <section class="noti-modal">
      <div class="modal-header">
        <div class="header1">알림</div>
        <button class="cancel-button" @click="emit('close')">x</button>
      </div>
      <div class="modal-body">
        <div v-if="notifications.length === 0" class="empty-message-wrapper">
          <div class="text-gray-400 text-sm text-center py-2">도착한 알림이 없습니다</div>
        </div>
        <div v-else class="body-scroll" ref="scrollContainer">
          <NotificationList
            @close="emit('close')"
            :notifications="notifications"
            :is-modal-open="isModalOpen"
          />
          <div v-if="isLastPage" class="text-gray-400 text-sm text-center py-2">catchy</div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.modal-overlay {
  @apply fixed inset-0 bg-black-alpha-20 flex justify-center items-center z-50;
}

.noti-modal {
  @apply w-[480px] min-h-[40vh] max-h-[90vh] bg-white rounded-lg pb-5 px-5 text-center shadow-elevated flex flex-col;
}

.modal-header {
  @apply w-full flex justify-between items-center border-b pt-4 px-2;
}

.cancel-button {
  @apply bg-primary text-white text-body-sm py-1 px-3 mb-4 rounded-sm hover:bg-primary-hover;
}

.modal-body {
  @apply flex-1 flex flex-col;
}

.header1 {
  @apply text-body-lg font-bold p-3 text-left mb-2;
}

.body-scroll {
  @apply overflow-y-auto space-y-1 py-2 pl-1 max-h-[70vh];
}

.empty-message-wrapper {
  @apply flex-grow flex justify-center items-center;
}
</style>
