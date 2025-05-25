<template>
  <div class="flex h-full bg-gray-50">
    <ProfileMenu />
    <router-view />

    <div class="flex-1 p-6">
      <!-- ✅ 로딩 중 -->
      <div v-if="!user" class="flex justify-center">
        <div class="bg-white border rounded-xl shadow-sm p-10 max-w-xl w-full text-center">
          <p class="text-gray-400">프로필 정보를 불러오는 중...</p>
        </div>
      </div>

      <!-- ✅ 유저 데이터가 있을 때 -->
      <div v-else class="flex justify-center">
        <div class="w-full max-w-md bg-white border rounded-xl shadow-sm p-10">
          <!-- 프로필 헤더 -->
          <ProfileHeader :user="user" />

          <!-- 고양이 슬라이더 -->
          <PetSlider v-if="user?.cats?.length" :pets="user.cats" class="mt-6" />

          <!-- 피드 탭 -->
          <FeedTabs v-model:selectedTab="selectedTab" class="mt-6" />

          <!-- 썸네일 리스트 -->
          <div class="thumbnail-list mt-6 h-[300px] overflow-y-auto">
            <MyThumbnailList :selectedTab="selectedTab" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import ProfileMenu from '../components/ProfileMenu.vue';
import ProfileHeader from '../components/ProfileHeader.vue';
import PetSlider from '../components/PetSlider.vue';
import FeedTabs from '../components/FeedTabs.vue';
import MyThumbnailList from '@/features/profile/components/MyThumbnailList.vue';
import { fetchMyProfile } from '@/api/profile'; // ✅ 내 프로필 전용

const user = ref(null);
const selectedTab = ref('MyFeed');

const loadProfile = async () => {
  try {
    user.value = await fetchMyProfile();
    console.log('✅ 내 프로필 응답:', user.value);
  } catch (e) {
    console.error('❌ 내 프로필 로딩 실패:', e);
    user.value = null;
  }
};

onMounted(() => {
  loadProfile();
});
</script>
<style>
.thumbnail-list {
  @apply overflow-y-auto flex flex-wrap;
  -ms-overflow-style: none;
}

.thumbnail-list::-webkit-scrollbar {
  display: none;
}
</style>
