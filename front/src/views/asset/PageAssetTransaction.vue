<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>거래 내역</span>
      </div>
        <div class="contents-body table-responsive-xxl">
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">거래 번호</th>
              <th scope="col">타입</th>
              <th scope="col">거래 일시</th>
              <th scope="col">이름</th>
              <th scope="col">수량</th>
              <th scope="col">평균 구매액</th>
              <th scope="col">평균 판매액</th>
              <th scope="col">수익금</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr
              @click="
                this.$router.push({
                  name: 'PageBoardDetail',
                  params: { id: 2 },
                })
              "
            >
              <th scope="row">(transaction.idx)</th>
              <td>(transaction.type)</td>
              <td>(transaction.date)</td>
              <td>(transaction.asset.name)</td>
              <td>(transaction.amount)</td>
              <td>(transaction.price_avg)</td>              
              <!-- 타입이 판매일 때만 -->
              <td>거래 할 때 당시 가격</td>
              <!-- C:\Users\user\Desktop\ysh\pj\메모\계산\거래 내역_수익금  -->
              <!-- 저거 보셈 -->
              <td>수익금</td>
              <td></td>
            </tr>
          </tbody>
        </table>
      </div>
      </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  data() {
    return {
      profile: {
        name: "anonymous",
        img: require("../../assets/img/anonymous.png"),
      },
    };
  },
};
</script>
<style lang="scss" scoped>
.contents {
  margin: 0;
  width: 100%;
  &-head {
    display: flex;
    height: 10%;
    margin: 0;
    justify-content: normal;
    span {
      font-size: 4vw;
    }
  }

}
</style>
