<script setup>
import SignupButton from '@/features/member/components/WhiteButton.vue';
import GoogleIcon from '@/assets/images/member/google-pink.png';
import NaverIcon from '@/assets/images/member/n-pink.png';
import KakaoIcon from '@/assets/images/member/kakao-talk-pink.png';
</script>

<template>
  <div class="main-frame">
    <img class="icon-size" src="@/assets/logo.png" alt="icon image" />
    <div class="text-frame">
      <div class="text">
        세상의 모든 냥이, 여기에서 만나요 <br />
        Catchy
      </div>
    </div>
    <div class="buttons-frame">
      <SignupButton
        :iconSrc="GoogleIcon"
        text="Google로 시작하기"
        redirectUrl="http://localhost:8000/oauth2/authorization/google"
      />
      <SignupButton
        :iconSrc="NaverIcon"
        text="Naver로 시작하기"
        redirectUrl="http://localhost/boot/oauth2/authorization/naver"
      />
      <SignupButton
        :iconSrc="KakaoIcon"
        text="Kakao로 시작하기"
        redirectUrl="http://localhost/boot/oauth2/authorization/kakao"
      />
    </div>
  </div>
</template>

<style scoped>
.main-frame {
  height: 100vh;
  width: auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  gap: 32px;
  padding-top: 180px;
}

.icon-size {
  width: 100px;
  height: 100px;
}

.text-frame {
  height: 62px;
  width: 790px;
  display: flex;
  flex-direction: column;
  position: relative;
}

.text {
  color: #474747;
  text-align: center;
  vertical-align: text-top;
  font-size: 24px;
  line-height: 1.5;
  border-style: hidden;
  outline: none;
  width: 790px;
}

.buttons-frame {
  height: 142px;
  width: 175px;
  display: flex;
  flex-direction: column;
  gap: 11px;
}
</style>
