<script setup>
import { ref } from 'vue';
import FollowMemberList from '@/features/follow/components/FollowMemberList.vue';
import { startLoading } from '@/composable/useLoadingBar.js';
import { getNotifications } from '@/api/notification.js';
import { getFollower, getFollowing } from '@/api/follow.js';
import { useInfiniteScroll } from '@/composable/useInfiniteScroll.js';

const emit = defineEmits(['close']);

const { isFollowing } = defineProps({
  isFollowing: {
    type: Boolean,
    default: false,
  },
});

const scrollContainer = ref(null);

const fetchFn = async (page) => {
  try {
    startLoading();
    const { data } = await (isFollowing ? getFollowing(page) : getFollower(page));
    return data;
  } catch (e) {
    console.log(e + '팔로우 목록 초기 로드 실패');
  }
};

const { items: members, isLastPage } = useInfiniteScroll({
  fetchFn,
  scrollTargetRef: scrollContainer,
});
</script>

<template>
  <div class="modal-overlay" @click.self="emit('close')">
    <section class="noti-modal">
      <div class="modal-header">
        <div v-if="isFollowing" class="header1">팔로잉</div>
        <div v-else class="header1">팔로워</div>
        <button class="cancel-button" @click="emit('close')">x</button>
      </div>
      <div class="modal-body">
        <div v-if="members.length === 0" class="empty-message-wrapper">
          <div class="catchy-caption">
            {{ isFollowing ? '팔로잉이 없습니다.' : '팔로워가 없습니다.' }}
          </div>
        </div>

        <div v-else class="body-scroll" ref="scrollContainer">
          <FollowMemberList :members="members" :isFollowing="isFollowing" />
          <div v-if="isLastPage" class="catchy-caption">catchy</div>
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
  @apply w-[480px] min-h-[40vh] max-h-[90vh] bg-white rounded-lg pb-5 text-center shadow-elevated flex flex-col;
}

.modal-header {
  @apply w-full flex justify-between items-center border-b pt-4 px-4;
}

.cancel-button {
  @apply bg-primary text-white text-body-sm py-1 px-3 mb-4 rounded-sm hover:bg-primary-hover;
}

.modal-body {
  @apply flex flex-col flex-grow;
}

.header1 {
  @apply text-body-lg font-bold p-3 text-left mb-2;
}

.body-scroll {
  @apply overflow-y-auto space-y-1 max-h-[70vh];
}

.catchy-caption {
  @apply text-gray-400 text-sm text-center pt-2 pb-4;
}

.empty-message-wrapper {
  @apply flex-grow flex justify-center items-center;
}
</style>
