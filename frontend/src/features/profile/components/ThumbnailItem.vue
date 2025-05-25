<script setup>
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

const { item, selectedTab } = defineProps({
  item: {
    type: Object,
    required: true,
  },
  selectedTab: {
    type: String,
    required: true,
  },
  isOther: {
    type: Boolean,
    default: false,
  },
});

function handleItemClick() {
  const feedTabs = ['OtherFeed', 'MyFeed', 'LikedFeed'];
  const jjureTabs = ['OtherJjure', 'MyJjure', 'LikedJjure'];

  if (feedTabs.includes(selectedTab)) {
    router.push(`${route.path}/feed/${item.feedId}`);
  } else if (jjureTabs.includes(selectedTab)) {
    router.push(`${route.path}/jjure/${item.jjureId}`);
  }
}
</script>

<template>
  <div
    @click="handleItemClick"
    class="w-full aspect-square overflow-hidden m-1 cursor-pointer group"
  >
    <img
      :src="item.thumbnailUrl"
      alt="item.thumbnailUrl"
      class="object-cover w-full h-full transition-transform duration-300 group-hover:drop-shadow"
    />
  </div>
</template>

<style scoped></style>
