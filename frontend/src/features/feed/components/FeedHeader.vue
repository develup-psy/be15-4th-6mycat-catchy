<template>
  <div class="feed-header">
    <router-link
      :to="author.authorId == authStore.memberId ? '/profile' : `/members/${author.authorId}`"
    >
      <div class="author">
        <DefaultProfile :src="author.profileImageUrl" :size="24" />

        <span class="nickname" @click="() => console.log('authorId' + author.authorId)">{{
          author.nickname
        }}</span>
      </div>
    </router-link>
    <div class="flex gap-4 items-center">
      <span class="text-xs text-gray-400">{{ formattedTime }}</span>
      <EditDeleteDropdown v-if="mine" @edit="$emit('edit')" @delete="$emit('delete')" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import EditDeleteDropdown from '@/components/EditDeleteDropdown.vue';
import { useAuthStore } from '@/stores/auth.js';
import DefaultProfile from '@/components/defaultProfile/DefaultProfile.vue';

dayjs.extend(relativeTime);

const props = defineProps({
  author: Object,
  createdAt: String,
  mine: Boolean,
});
const formattedTime = computed(() => dayjs(props.createdAt).fromNow());
const authStore = useAuthStore();
</script>

<style scoped>
.feed-header {
  @apply flex items-center justify-between px-4 py-3;
}

.author {
  @apply flex items-center gap-3;
}

.author .nickname {
  @apply text-xs font-semibold;
}
</style>
