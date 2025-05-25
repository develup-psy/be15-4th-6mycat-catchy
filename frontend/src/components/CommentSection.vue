<script setup>
import { ref, computed, onMounted, nextTick } from 'vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { getComments, postComment, deleteComment } from '@/api/comment';
import { useAuthStore } from '@/stores/auth';

dayjs.extend(relativeTime);

const props = defineProps({
  targetId: Number,
  targetType: String,
});

const authStore = useAuthStore();
const currentUserId = computed(() => authStore.memberId);
const comments = ref([]);
const newComment = ref('');
const replyingTo = ref(null);
const commentInput = ref(null);

const fetchComments = async () => {
  const { data } = await getComments(props.targetId, props.targetType, 1, 100);
  const flatList = data.data.content;
  flatList.forEach((c) => (c.showReplies = false));
  comments.value = flatList;
};

onMounted(fetchComments);

const addComment = async () => {
  const text = newComment.value.trim();
  if (!text) return;

  const isReply = replyingTo.value && newComment.value.startsWith('@');

  await postComment(
    props.targetId,
    props.targetType,
    text,
    isReply ? replyingTo.value : null,
    currentUserId,
  );

  newComment.value = '';
  replyingTo.value = null;
  await fetchComments();
};

const deleteCommentHandler = async (commentId) => {
  await deleteComment(commentId, props.targetType, currentUserId);
  await fetchComments();
};

const handleReplyClick = (commentId, writer) => {
  replyingTo.value = commentId;
  newComment.value = writer ? `@${writer} ` : '';
  nextTick(() => {
    commentInput.value?.focus();
  });
};

const toggleReplies = (comment) => {
  const index = comments.value.findIndex((c) => c.commentId === comment.commentId);
  if (index === -1) return;

  const updated = { ...comments.value[index] };
  updated.showReplies = !updated.showReplies;
  comments.value.splice(index, 1, updated);
};

const formatMention = (text) => {
  return text.replace(/(@\S+)/g, '<span class="mention">$1</span>');
};

const filteredComments = computed(() => {
  const root = [];
  const map = {};

  comments.value.forEach((c) => {
    map[c.commentId] = {
      ...c,
      replies: c.replies || [],
      showReplies: c.showReplies ?? false,
    };
  });

  comments.value.forEach((c) => {
    if (c.parentCommentId) {
      const parent = map[c.parentCommentId];
      const child = map[c.commentId];
      if (parent && child) {
        parent.replies.push(child);
      }
    } else {
      root.push(map[c.commentId]);
    }
  });

  return root;
});
</script>

<template>
  <div class="w-full h-full overflow-hidden border-none bg-white flex flex-col">
    <div class="flex-1 overflow-y-auto p-4 scrollbar-hide">
      <div class="flex flex-col gap-6">
        <div v-for="comment in filteredComments" :key="comment.commentId" class="text-sm">
          <div class="flex flex-col">
            <p class="flex items-center gap-2">
              <router-link
                :to="`/members/${comment.memberId}`"
                class="text-xs text-[#3e2f2f] font-bold hover:underline"
              >
                {{ comment.nickname }}
              </router-link>
              <span class="text-xs text-gray-400">{{ dayjs(comment.createdAt).fromNow() }}</span>
              <button
                v-if="Number(comment.memberId) === Number(currentUserId)"
                @click="deleteCommentHandler(comment.commentId)"
                class="bg-none border-none text-gray-400 text-base leading-none px-1 ml-auto hover:text-red-500"
              >
                ×
              </button>
            </p>
            <p
              class="mt-0.5 text-[#555555] font-normal text-sm whitespace-pre-wrap"
              v-html="formatMention(comment.content)"
            ></p>
            <div class="flex gap-2 mt-1">
              <button
                @click="handleReplyClick(comment.commentId, comment.nickname)"
                class="bg-none border-none text-gray-400 text-xs cursor-pointer p-0 hover:underline"
              >
                답글 달기
              </button>
              <button
                v-if="comment.replies.length"
                @click="toggleReplies(comment)"
                class="bg-none border-none text-gray-400 text-xs cursor-pointer p-0 hover:underline"
              >
                {{ comment.showReplies ? '답글 숨기기' : '답글 보기' }}
              </button>
            </div>
          </div>

          <div v-if="comment.showReplies" class="ml-5 mt-2.5 flex flex-col gap-3">
            <div v-for="reply in comment.replies" :key="reply.commentId">
              <p class="flex items-center gap-2">
                <router-link
                  :to="`/members/${reply.nickname}`"
                  class="text-[#3e2f2f] text-xs font-bold hover:underline"
                >
                  {{ reply.nickname }}
                </router-link>
                <span class="text-xs text-gray-400">{{ dayjs(reply.createdAt).fromNow() }}</span>
                <button
                  v-if="Number(reply.memberId) === Number(currentUserId)"
                  @click="deleteCommentHandler(reply.commentId)"
                  class="bg-none border-none text-gray-400 text-base leading-none px-1 ml-auto hover:text-red-500"
                >
                  ×
                </button>
              </p>
              <p
                class="mt-0.5 text-[#555555] text-sm font-normal whitespace-pre-wrap"
                v-html="formatMention(reply.content)"
              ></p>
              <div class="flex gap-2 mt-1">
                <button
                  @click="handleReplyClick(comment.commentId, reply.nickname)"
                  class="bg-none border-none text-gray-400 text-xs cursor-pointer p-0 hover:underline"
                >
                  답글 달기
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="flex gap-2 px-2 py-3 border-t border-gray-300">
      <input
        ref="commentInput"
        v-model="newComment"
        type="text"
        placeholder="댓글 달기..."
        @keyup.enter="addComment"
        class="flex-1 px-2 py-1.5 text-sm"
      />
      <button
        @click="addComment"
        :disabled="!newComment.trim()"
        class="bg-transparent border-none text-[#007bff] font-bold text-sm cursor-pointer disabled:text-gray-300 disabled:cursor-default"
      >
        게시
      </button>
    </div>
  </div>
</template>

<style scoped>
::v-deep(.mention) {
  color: #ff5c8d;
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
.scrollbar-hide {
  -ms-overflow-style: none; /* IE/Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
