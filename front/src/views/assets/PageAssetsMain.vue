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
          <div class="domestic" @click="this.$router.push('/assets/stocks/domestic')">
            <h1>국내 주식</h1>
            <span>여기다 인덱스 넣으셈</span>
          </div>
          <div class="overseas" @click="this.$router.push('/assets/stocks/overseas')">
            <h1>해외 주식</h1>
            <span>여기도 인덱스 넣으셈</span>
          </div>
        </div>
        <div class="box currencies">
          <h1>외화</h1>
          <span>으엑으에에에</span>
        </div>
        <div class="box materials">
          <h1>원자재</h1>
          <span>여긴 뭘 넣어야됨?</span>
        </div>
        <div class="box bitcoins">
          <h1>비트코인</h1>
          <span>여긴 뭘 넣어야됨?</span>
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
  methods: {},
};
</script>
<style lang="scss" scoped>
* {
  
}
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
    .stocks {
      div {
        height: 50%;
      }
    }
  }
}
</style>
