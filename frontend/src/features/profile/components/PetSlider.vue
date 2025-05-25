<script setup>
import { ref, computed } from 'vue'
import PetCard from './PetCard.vue'
import LeftArrowIcon from '@/components/icons/IconLeftArrow.vue'
import RightArrowIcon from '@/components/icons/IconRightArrow.vue'

const props = defineProps({
  pets: {
    type: Array,
    required: true
  }
})

const currentIndex = ref(0)
const currentPet = computed(() => props.pets[currentIndex.value])

function nextPet() {
  if (currentIndex.value < props.pets.length - 1) {
    currentIndex.value++
  }
}

function prevPet() {
  if (currentIndex.value > 0) {
    currentIndex.value--
  }
}
</script>

<template>
  <div class="flex justify-center w-full">
    <div class="relative w-[400px]"> <!-- 카드+화살표 감싸는 박스 -->

      <!-- 카드 -->
      <PetCard :pet="currentPet" />

      <!-- 왼쪽 화살표 -->
      <button
        v-if="currentIndex > 0"
        class="absolute left-0 top-1/2 -translate-y-1/2 text-gray-400 hover:text-pink-400"
        @click="prevPet"
      >
        <LeftArrowIcon class="w-6 h-6" />
      </button>

      <!-- 오른쪽 화살표 -->
      <button
        v-if="currentIndex < pets.length - 1"
        class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-400 hover:text-pink-400"
        @click="nextPet"
      >
        <RightArrowIcon class="w-6 h-6" />
      </button>

    </div>
  </div>
</template>


