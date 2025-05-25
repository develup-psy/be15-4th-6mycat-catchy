<script setup>
import SidebarNav from '@/components/layout/SidebarNav.vue';

const emit = defineEmits(['open-upload-modal', 'open-notification-modal']);

function handleOpenUploadModal() {
  emit('open-upload-modal');
}

function handleNotificationModal() {
  emit('open-notification-modal');
}
</script>

<template>
  <div class="layout">
    <aside class="sidebar">
      <router-link to="/" class="logo">
        <img src="@/assets/logo.png" alt="Catchy 로고" class="logo-image" />
        <span>Catchy</span>
      </router-link>

      <SidebarNav
        @open-upload-modal="handleOpenUploadModal"
        @open-notification-modal="handleNotificationModal"
      />
    </aside>

    <main class="main-content">
      <slot />
    </main>
  </div>
</template>

<style scoped>
.layout {
  @apply flex h-screen w-full bg-pink-50 text-gray-800;
}

.sidebar {
  @apply w-[250px] px-5 py-8 bg-white border-r border-gray-200 flex flex-col items-center font-bold;
}

.logo {
  @apply flex flex-col items-center gap-2 mb-10;
}

.logo-image {
  @apply w-[60px] h-[60px] object-cover rounded-full;
}

.logo span {
  @apply text-headline-sm text-black font-bold;
}

.main-content {
  @apply flex-1 overflow-y-auto bg-white;
}
</style>
