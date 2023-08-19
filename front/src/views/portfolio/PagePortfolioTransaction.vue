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
              <td>{{ transaction.type }}</td>
              <td>{{ transaction.transactionDate }}</td>
              <td>{{ transaction.assetName }}</td>
              <td>{{ transaction.amount }}</td>
              <td>{{ transaction.priceAvg }}</td>              
              <td>{{ transaction.profit }}</td>              
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
    if(localStorage.getItem("portfolioIdx") == null){
      alert('먼저 포트폴리오를 선택해주세요.');
      this.$router.push('/portfolio');
    }
    this.getAllTransaction();
  },
  methods :{
    getAllTransaction() {
        this.$axios
        .get(`/api/v1/transaction`, {
            params : {
                portfolioIdx: localStorage.getItem("portfolioIdx")
            }
        }).then((res) => {
          if (res.data.code === 0) {
            console.log(res.data)
            this.transactionList = res.data.data.transactionList;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    }
  }
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
