<script setup>
import ProfileMenu from '@/features/profile/components/ProfileMenu.vue';
import MemberInfoContent from '@/features/member/components/MemberInfoContent.vue';
</script>

<template>
  <div class="container bg-gray-50">
    <ProfileMenu />
    <div class="content-wrapper">
      <div class="content-inner">
        <MemberInfoContent />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  height: 100vh;
}

.content-wrapper {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  overflow: auto;
  padding-top: 10px;
}

.content-inner {
  padding-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}
</style>
