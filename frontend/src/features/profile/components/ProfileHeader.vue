<template>
  <div class="flex justify-center relative">
    <!-- 🔘 우측 상단 차단 or 차단 해제 버튼 -->
    <button
      v-if="Number(currentUserId) !== Number(user.member.id)"
      class="absolute top-1 right-0 text-pink-500 text-sm font-semibold hover:underline"
      @click="handleBlock"
    >
      {{ isBlocked ? '차단 해제' : '🚫 차단' }}
    </button>
    <button
      v-if="Number(currentUserId) !== Number(user.member.id) && currentUserId"
      class="absolute top-7 right-0 text-pink-500 text-sm font-semibold hover:underline"
      @click="handleFollow"
    >
      {{ currentIsFollowing ? '팔로잉' : '팔로우' }}
    </button>

    <div v-if="user?.member" class="flex items-start gap-6 mb-4 -translate-x-12">
      <!-- 프로필 이미지 -->
      <DefaultProfile :src="user.member.profileImage" :size="96" class="shrink-0" />

      <div class="flex-1">
        <!-- 닉네임 -->
        <h2 class="text-headline-lg font-bold">{{ user.member.nickname }}</h2>

        <!-- 뱃지 -->
        <div class="flex gap-2 mt-1">
          <span v-if="user.badges?.topRanker" class="badge bg-pink-300 text-white"
            >🏅 랭킹 1등</span
          >
          <span v-if="user.badges?.influencer" class="badge bg-pink-400 text-white"
            >⭐ 인증유저</span
          >
          <span v-if="user.badges?.birthday" class="badge bg-pink-200 text-white">🎂 생일</span>
        </div>

        <!-- 상태 메시지 -->
        <p class="text-body-sm text-gray-400 mt-1">
          {{ user.member.statusMessage || '상태메시지가 없습니다.' }}
        </p>

        <!-- 게시물 수 등 -->
        <div class="flex gap-4 mt-2 text-body-md text-gray-700">
          <span>게시물 {{ user.contents?.feedCount ?? 0 }}</span>
          <span>
            <button class="follow-button" @click="handleGetFollower" :disabled="isOther">
              팔로워 {{ user.follows?.followerCount ?? 0 }}
            </button>
          </span>
          <span>
            <button class="follow-button" @click="handleGetFollowing" :disabled="isOther">
              팔로잉 {{ user.follows?.followingCount ?? 0 }}
            </button>
          </span>
        </div>
      </div>
    </div>
  </div>

  <!-- 팔로우 모달 -->
  <template v-if="isVisibleFollowModal">
    <MyFollowModal :isFollowing="isFollowing" @close="isVisibleFollowModal = false" />
  </template>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import MyFollowModal from '@/features/follow/components/MyFollowModal.vue';
import { useAuthStore } from '@/stores/auth.js';
import { blockUser, unblockUser, fetchBlockedUsers } from '@/api/block.js';
import DefaultProfile from '@/components/defaultProfile/DefaultProfile.vue';
import { requestFollow, unfollow } from '@/api/follow.js';
import { showSuccessToast } from '@/utills/toast.js';

const { user, isOther } = defineProps({
  user: {
    type: Object,
    required: true,
  },
  isOther: {
    type: Boolean,
    default: false,
  },
});

const authStore = useAuthStore();
const currentUserId = computed(() => authStore.memberId);

const isFollowing = ref(false);
const isVisibleFollowModal = ref(false);
const isBlocked = ref(false);

const currentIsFollowing = ref(user.isFollowing);

function handleGetFollower() {
  isFollowing.value = false;
  isVisibleFollowModal.value = true;
}

function handleGetFollowing() {
  isFollowing.value = true;
  isVisibleFollowModal.value = true;
}

// ✅ 차단 여부 확인
onMounted(async () => {
  if (isOther) return;
  try {
    const { data } = await fetchBlockedUsers(currentUserId.value, 1, 100);
    const blockedList = data.data.content || [];
    isBlocked.value = blockedList.some((u) => Number(u.blockedId) === Number(user.member.id));
  } catch (error) {
    console.error('❌ 차단 목록 조회 실패:', error);
  }
});

// ✅ 차단/해제 처리
const handleBlock = async () => {
  if (isOther) {
    console.log('다른사람 프로필입니다. 차단불가');
  }
  try {
    if (isBlocked.value) {
      await unblockUser(currentUserId.value, user.member.id);
      alert('차단 해제되었습니다.');
    } else {
      await blockUser(currentUserId.value, user.member.id);
      alert('차단되었습니다.');
    }
    isBlocked.value = !isBlocked.value;
  } catch (error) {
    console.error('❌ 차단/해제 실패:', error);
    alert('차단 상태 변경에 실패했습니다.');
  }
};

const handleFollow = async () => {
  currentIsFollowing.value = !currentIsFollowing.value;

  try {
    if(currentIsFollowing.value) {
      await requestFollow(user.member.id);
    } else {
      await unfollow(user.member.id);
    }
  } catch (e) {
    console.log('팔로우 에러', e);
  } finally {
    showSuccessToast(currentIsFollowing.value ? '팔로우 완료!' : '팔로우 취소 완료!');
  }
}

</script>

<style scoped>
.badge {
  @apply text-xs font-semibold px-2 py-1 rounded-full;
}

.follow-button {
  @apply hover:text-primary-hover;
}

.follow-button:disabled {
  @apply cursor-not-allowed text-gray-400;
}
</style>
