<template>
  <div class="text-headline-md font-bold mb-6">차단 목록</div>
  <ul class="space-y-4">
    <li
        v-for="user in blockedUsers"
        :key="user.blockedId"
        class="flex items-center justify-between"
    >
      <div class="flex items-center gap-4">
        <DefaultProfile :src="user.profileImage" :size="40" />
        <div>
          <p class="font-semibold text-sm">{{ user.blockedNickname }}</p>
          <p class="text-gray-500 text-xs">{{ formatDate(user.blockedAt) }}</p>
        </div>
      </div>
      <button
          class="bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-1.5 rounded text-sm"
          @click="unblock(user.blockedId)"
      >
        해제
      </button>
    </li>
    <li v-if="blockedUsers.length === 0" class="text-center text-gray-500 text-sm py-2">
      차단한 사용자가 없습니다.
    </li>
  </ul>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import dayjs from 'dayjs';
import { fetchBlockedUsers, unblockUser } from '@/api/block.js';
import { useAuthStore } from '@/stores/auth.js';
import DefaultProfile from '@/components/defaultProfile/DefaultProfile.vue'; // 경로는 실제 위치에 맞게 조정

const blockedUsers = ref([]);
const authStore = useAuthStore();
const userId = computed(() => authStore.memberId);

const loadBlockedUsers = async () => {
  try {
    const res = await fetchBlockedUsers(userId.value, 1, 50);
    blockedUsers.value = res.data.data.content;
  } catch (error) {
    console.error('❌ 차단 목록 불러오기 실패:', error);
  }
};

const unblock = async (blockedId) => {
  try {
    await unblockUser(userId.value, blockedId);
    blockedUsers.value = blockedUsers.value.filter((user) => user.blockedId !== blockedId);
  } catch (error) {
    console.error('❌ 차단 해제 실패:', error);
  }
};

const formatDate = (datetime) => dayjs(datetime).format('YYYY.MM.DD HH:mm');

onMounted(loadBlockedUsers);
</script>
