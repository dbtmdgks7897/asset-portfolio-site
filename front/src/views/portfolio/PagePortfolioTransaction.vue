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
              <th scope="col">평균 거래액</th>
              <th scope="col">총 거래액</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr v-for="transaction in transactionList" :key="transaction">
              <th scope="row">{{ transaction.idx }}</th>
              <td :style="getTypeStyle(transaction.type)" style="font-weight: bold;">
                {{ transaction.type }}
              </td>
              <td>{{ transaction.transactionDate }}</td>
              <td>{{ transaction.assetName }}</td>
              <td>{{ transaction.amount.toLocaleString() }}</td>
              <td>{{ transaction.priceAvg.toLocaleString() }}</td>
              <td>{{ transaction.profit.toLocaleString() }}</td>
              <!-- 타입이 판매일 때만 -->
              <!-- C:\Users\user\Desktop\ysh\pj\메모\계산\거래 내역_수익금  -->
              <!-- 저거 보셈 -->
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
      transactionList: null,
    };
  },
  mounted() {
    if (sessionStorage.getItem("portfolioIdx") == null) {
      alert("먼저 포트폴리오를 선택해주세요.");
      this.$router.push("/portfolio");
    }
    this.getAllTransaction();
  },
  methods: {
    getAllTransaction() {
      this.$axios
        .get(`/api/v1/transaction`, {
          params: {
            portfolioIdx: sessionStorage.getItem("portfolioIdx"),
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data);
            this.transactionList = res.data.data.transactionList;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          if(err.response.status == 500){
            alert("로그인 해주세요")
            this.$router.push("/login")
          }
        });
    },
    getTypeStyle(type) {
      const style = {};

      if (type === "구입") {
        style.color = "green"; // 구매일 경우 초록색 글자
      } else if (type === "판매") {
        style.color = "red"; // 판매일 경우 빨간색 글자
      }
      return style;
    },
  },
};
</script>
<style lang="scss" scoped>
.contents {
  margin: 0;
  width: 100%;
  height: 100%;
  min-height: 100vh;
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
