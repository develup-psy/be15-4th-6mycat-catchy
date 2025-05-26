<script setup>
import { RouterLink } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useAuthStore } from '@/stores/auth';
import { useDefaultProfileStore } from '@/stores/defaultProfileStore.js';
import { startLoading, stopLoading } from '@/composable/useLoadingBar.js';
import DefaultProfile from '@/components/defaultProfile/DefaultProfile.vue';
import { hasUnreadNotification } from '@/features/notification/utils/notificationBadge';
import { computed } from 'vue';

const emit = defineEmits(['open-upload-modal', 'open-notification-modal']);

function handleCreateClick() {
  emit('open-upload-modal');
}

function handleNotificationModal() {
  emit('open-notification-modal');
}

const navItems = [
  { label: '피드', icon: 'fas fa-home', path: '/feed' },
  { label: '쮸르', icon: 'fa-solid fa-circle-play', path: '/jjure' },
  { label: '게임', icon: 'fas fa-gamepad', path: '/game' },
  { label: '알림', icon: 'fas fa-bell', type: 'noti-modal' },
  { label: '만들기', icon: 'fa-solid fa-square-plus', type: 'modal' },
];

const authStore = useAuthStore();
const { isAuthenticated } = storeToRefs(authStore);

const defaultProfileStore = useDefaultProfileStore();
const { image: profileImage, nickname } = storeToRefs(defaultProfileStore);

function handleLogout() {
  startLoading();
  authStore.logout().finally(() => {
    stopLoading();
    location.reload();
  });
}

const showBadge = computed(() => hasUnreadNotification.value);

</script>

<template>
  <nav class="nav">
    <ul class="nav-list">
      <li v-for="(item, index) in navItems" :key="index">
        <RouterLink v-if="!item.type" :to="item.path" class="nav-item">
          <i :class="item.icon" />
          <span>{{ item.label }}</span>
        </RouterLink>

        <button
          v-else-if="item.type === 'modal'"
          type="button"
          class="nav-item"
          @click="handleCreateClick"
        >
          <i :class="item.icon" />
          <span>{{ item.label }}</span>
        </button>

        <button
          v-else-if="item.type === 'noti-modal'"
          type="button"
          class="nav-item relative"
          @click="handleNotificationModal"
        >
          <i :class="item.icon" />
          <span>{{ item.label }}</span>

          <!-- 알림 뱃지 -->
          <span
            v-if="showBadge"
            class="absolute top-1 right-1 w-2.5 h-2.5 rounded-full bg-red-500"
          ></span>
        </button>
      </li>
    </ul>

    <footer class="threads" v-if="isAuthenticated">
      <DefaultProfile :src="profileImage" :size="24" />
      <RouterLink to="/profile" class="hover:text-[#FF5C8D]">{{ nickname }}</RouterLink>
      <span class="logout" @click="handleLogout">로그아웃</span>
    </footer>

    <footer class="threads" v-else>
      <RouterLink to="/member/start">Catchy 시작하기</RouterLink>
    </footer>
  </nav>
</template>

<style scoped>
.nav {
  @apply flex flex-col justify-between items-center w-full h-full;
}

.nav-list {
  @apply w-full;
}

.nav-item {
  @apply flex items-center w-full text-body-md text-gray-700 py-2 px-1 transition-colors;
}
.nav-item i {
  @apply w-5 mr-2 text-primary;
}
.nav-item:hover {
  @apply text-primary;
}

.threads {
  @apply font-bold text-sm text-gray-700 flex justify-center flex-wrap gap-2 pb-4;
  align-items: flex-start;
  width: 100%;
  text-align: center;
}
.threads img {
  @apply w-6 h-6 rounded-full;
}
.logout {
  @apply text-red-500 cursor-pointer;
  white-space: nowrap;
}
</style>
