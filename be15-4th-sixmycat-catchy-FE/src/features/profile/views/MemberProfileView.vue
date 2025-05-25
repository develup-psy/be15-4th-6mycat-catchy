<template>
  <router-view />
  <div class="flex h-full bg-gray-50">
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
          <ProfileHeader :user="user" :is-other="true" />

          <!-- 고양이 슬라이더 -->
          <PetSlider v-if="user?.cats?.length" :pets="user.cats" class="mt-6" />

          <!-- 피드 탭 -->
          <FeedTabs v-model:selectedTab="selectedTab" class="mt-6" :is-other="true" />

          <!-- 썸네일 리스트 -->
          <div class="thumbnail-list mt-6 h-[300px] overflow-y-auto">
            <OtherThumbnailList :selectedTab="selectedTab" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import ProfileHeader from '../components/ProfileHeader.vue';
import PetSlider from '../components/PetSlider.vue';
import FeedTabs from '../components/FeedTabs.vue';
import { fetchUserProfile } from '@/api/profile';
import OtherThumbnailList from '@/features/profile/components/OtherThumbnailList.vue';

const route = useRoute();
const user = ref(null);
const selectedTab = ref('OtherFeed');

// 타 회원 닉네임으로 프로필 조회
const loadProfile = async () => {
  const id = route.params.mid;
  console.log(id);

  if (!id) {
    console.warn('❌ member id가 정의되지 않았습니다. 경로를 확인하세요.');
    return;
  }

  try {
    user.value = await fetchUserProfile(id);
    console.log('✅ 프로필 응답:', user.value);
  } catch (e) {
    console.error('❌ 프로필 로딩 실패:', e.response?.data || e.message);
    user.value = null;
  }
};

onMounted(() => {
  loadProfile();
});

watch(
  () => route.params.mid,
  () => {
    loadProfile();
  },
);
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
