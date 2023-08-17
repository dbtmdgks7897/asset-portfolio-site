<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head">
        <span>자산 검색</span>
      </div>
      <div class="contents-body grid">
        <div class="box stocks">
          <div
            style="border-bottom: 1px solid"
            class="domestic"
            @click="this.$router.push('/asset/stock/domestic')"
          >
            <h1>국내 주식</h1>
          </div>
          <div
            class="overseas"
            @click="this.$router.push('/asset/stock/overseas')"
          >
            <h1>해외 주식</h1>
          </div>
        </div>
        <div
          class="box currencies"
          @click="this.$router.push('/asset/currency')"
        >
          <h1>외화</h1>
        </div>
        <div class="box materials"
        @click="this.$router.push('/asset/bitcoin')">
          <h1>비트코인</h1>
        </div>
        <div class="box bitcoins">
          <h1>커스텀</h1>
        </div>
      </div>
    </div>
  </div>
  <!-- 로그인 시에만 보이게 -->
</template>

<script setup>
import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  data() {
    //변수생성
    return {};
  },
  mounted() {
    if(localStorage.getItem("portfolioIdx") == null){
      alert('먼저 포트폴리오를 선택해주세요.');
      this.$router.push('/portfolio');
    }
  },
  methods: {},
};
</script>
<style lang="scss" scoped>
.contents {
  height: 100vh;
  &-head {
    height: 10%;
  }
  &-body {
    width: 100%;
    height: 100%;
    grid-template-columns: 25vw 25vw;
    grid-template-rows: 35vh 35vh;
    justify-content: space-around;
    gap: 5%;
    .box {
      border: 2px solid black;
      width: 100%;
      height: 100%;
    }
    .currencies,
    .materials,
    .bitcoins {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      transition: all 0.3s; // 전환 효과 추가
      &:hover {
        background-color: gray; // 회색 배경
        box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.5); // 안쪽 그림자
      }
    }
    .stocks {
      div {
        height: 50%;
        display: flex;
        flex-direction: column;
        justify-content: center;

        transition: all 0.3s; // 전환 효과 추가
        &:hover {
          background-color: gray; // 회색 배경
          box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.5); // 안쪽 그림자
        }
      }
    }
  }
}
</style>
