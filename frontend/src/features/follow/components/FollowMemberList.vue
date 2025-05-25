<script setup>
import NotificationItem from '@/features/notification/components/NotificationItem.vue';
import FollowMemberItem from '@/features/follow/components/FollowMemberItem.vue';

const { members, isFollowing } = defineProps({
  members: {
    type: Array,
    required: true,
  },
  isFollowing: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <div class="space-y-2 max-height border-b border-gray-200 overflow-y-auto">
    <FollowMemberItem
      v-for="(member, index) in members"
      :key="index"
      :member="member"
      :isFollowing="isFollowing"
    />
  </div>
</template>

<style scoped></style>
