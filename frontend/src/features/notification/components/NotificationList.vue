<script setup>
import NotificationItem from '@/features/notification/components/NotificationItem.vue';

const { notifications, isModalOpen } = defineProps({
  notifications: {
    type: Array,
    required: true,
  },
  isModalOpen: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['close']);
</script>

<template>
  <div v-if="notifications.length !== 0" class="text-gray-500 text-start">이번 달</div>
  <div class="space-y-4 max-height overflow-y-auto">
    <NotificationItem
      @close="emit('close')"
      v-for="notification in notifications"
      :key="notification.id"
      :notification="notification"
      :is-modal-open="isModalOpen"
    />
  </div>
</template>

<style scoped></style>
