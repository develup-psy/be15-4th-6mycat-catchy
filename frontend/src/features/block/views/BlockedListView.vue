<template>
  <div class="flex bg-gray-50 h-full">
    <ProfileMenu />
    <div class="flex-1 p-8">
      <div class="flex justify-center">
        <div class="w-full max-w-[800px] background">
          <BlockList />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ProfileMenu from '../../profile/components/ProfileMenu.vue';
import BlockList from '@/features/block/components/BlockList.vue';
</script>

<style>
.background {
  background-color: #ffffff;
  height: auto;
  width: 565px;
  filter: drop-shadow(0px 4px 12px rgba(0, 0, 0, 0.04));
  border-radius: 12px;
  padding: 25px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
