<!-- src/features/profile/components/ProfileMenu.vue -->
<template>
  <aside class="w-64 p-4 border-r">
    <h2 class="text-lg font-semibold mb-4">계정</h2>
    <ul class="space-y-2">
      <li>
        <RouterLink
          to="/profile"
          class="block px-3 py-2 rounded hover:bg-pink-100"
          :class="{ 'bg-pink-100 text-pink-600 font-bold': isActive('/profile') }"
        >
          프로필
        </RouterLink>
      </li>
      <li>
        <RouterLink
          to="/profile/edit"
          class="block px-3 py-2 rounded hover:bg-pink-100"
          :class="{ 'bg-pink-100 text-pink-600 font-bold': isActive('/profile/edit') }"
        >
          프로필 수정
        </RouterLink>
      </li>
      <li>
        <RouterLink
          to="/member/info"
          class="block px-3 py-2 rounded hover:bg-pink-100"
          :class="{ 'bg-pink-100 text-pink-600 font-bold': isActive('/member/info') }"
        >
          개인 정보
        </RouterLink>
      </li>
      <li>
        <RouterLink
          to="/profile/block"
          class="block px-3 py-2 rounded hover:bg-pink-100"
          :class="{ 'bg-pink-100 text-pink-600 font-bold': isActive('/profile/block') }"
        >
          차단 목록
        </RouterLink>
      </li>
    </ul>
  </aside>
</template>

<script setup>
import { useRoute } from 'vue-router';

const route = useRoute();

// 현재 경로에 따라 활성화된 메뉴 표시
const isActive = (path) => {
  return route.path === path;
};
</script>
