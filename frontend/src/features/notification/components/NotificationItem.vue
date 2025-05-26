<script setup>
import { ref, toRef, computed, watch } from 'vue';
import { showSuccessToast } from '@/utills/toast.js';
import { requestFollow, unfollow } from '@/api/follow.js';
import DefaultProfile from '@/components/defaultProfile/DefaultProfile.vue';
import { useRouter } from 'vue-router';

const props = defineProps({
  notification: {
    type: Object,
    required: true,
  },
  isModalOpen: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['close']);

const isModalOpenRef = toRef(props, 'isModalOpen');
const showFollow = ref(props.notification.type === 'FOLLOW');
const initialIsFollowing = ref(props.notification.initialFollowing);
const currentIsFollowing = ref(props.notification.initialFollowing);
const router = useRouter();
const type = props.notification.type;

const timeAgo = computed(() => {
  const now = new Date();
  const created = new Date(props.notification.createdAt);
  const diff = now - created;

  const seconds = Math.floor(diff / 1000);
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);
  const weeks = Math.floor(days / 7);

  if (seconds < 60) return `${seconds}초`;
  if (minutes < 60) return `${minutes}분`;
  if (hours < 24) return `${hours}시간`;
  if (days < 7) return `${days}일`;
  return `${weeks}주`;
});

const notificationText = computed(() => {
  const base = `님이 회원님`;
  switch (type) {
    case 'FOLLOW':
      return `${base}을 팔로우하기 시작했습니다.`;
    case 'COMMENT':
      return `${base}의 게시물에 댓글을 남겼습니다.`;
    case 'RECOMMENT':
      return `${base}의 댓글에 답글을 남겼습니다.`;
    case 'FEED_LIKE':
      return `${base}의 피드를 좋아합니다.`;
    case 'JJURE_LIKE':
      return `${base}의 쭈르를 좋아합니다.`;
    case 'BIRTHDAY':
      return `회원님의 냥이 생일 축하해요! 🐾🎂🐱🎉`;
    default:
      return '';
  }
});

function goToProfile() {
  emit('close');
  router.push(`/members/${props.notification.senderId}`);
}

function goToRelatedFeed() {
  let url = '';

  const type = props.notification.type;

  switch (type) {
    case 'FOLLOW':
      url = `/members/${props.notification.senderId}`;
      break;
    case 'COMMENT':
    case 'RECOMMENT':
      // 댓글/답글 알림은 아직 이동 페이지 없음 → 비워둠
      return;
    case 'FEED_LIKE':
      url = `/feed/${props.notification.relatedId}`;
      break;
    case 'JJURE_LIKE':
      url = `/jjure/${props.notification.relatedId}`;
      break;
    case 'BIRTHDAY':
      url = `/profile`;
      break;
    default:
      return;
  }

  emit('close');
  router.push(url);
}

function toggleFollow() {
  currentIsFollowing.value = !currentIsFollowing.value;
  showSuccessToast(currentIsFollowing.value ? '팔로우 완료!' : '팔로우 취소 완료!');
}

async function handleFollowAPI() {
  if (initialIsFollowing.value === currentIsFollowing.value) return;

  if (currentIsFollowing.value) {
    await requestFollow(props.notification.senderId);
  } else {
    await unfollow(props.notification.senderId);
  }
}

watch(isModalOpenRef, (newVal, oldVal) => {
  if (oldVal && !newVal) {
    handleFollowAPI();
  }
});
</script>

<template>
  <div class="flex items-center gap-2 cursor-pointer" @click="goToRelatedFeed">
    <DefaultProfile :src="props.notification.profileImage" :size="56" @click="goToProfile" />
    <div class="text-start text-sm leading-snug flex-1">
      <span class="font-bold">{{ props.notification.senderNickname }}</span>
      <span>{{ notificationText }}</span>
      <span class="text-gray-300 pl-1">{{ timeAgo }}</span>
    </div>
    <button
      v-if="showFollow"
      class="following-button"
      @click.stop="toggleFollow"
      :class="
        currentIsFollowing
          ? 'bg-secondary hover:bg-secondary-hover text-white'
          : 'bg-primary hover:bg-primary-hover text-white'
      "
    >
      {{ currentIsFollowing ? '팔로잉' : '팔로우' }}
    </button>
  </div>
</template>

<style scoped>
.following-button {
  @apply text-body-sm py-1 px-3 rounded-sm;
}
</style>
