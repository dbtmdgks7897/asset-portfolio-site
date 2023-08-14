<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>국내 주식
          <a style="font-size: 3vh;" href="https://kr.investing.com/stock-screener/?sp=country::11|sector::a|industry::a|equityType::a%3Ceq_market_cap;1" target="_blank">종목 코드 검색</a>
        </span>
        
        <div class="input-group mb-1">
          <input
            type="text"
            v-model="stockCode"
            class="form-control"
            placeholder="종목 코드로만 검색 가능"
            aria-label="검색어"
            aria-describedby="button-addon2"
            @keydown.enter="getDomesticStockSearchData"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/boards 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
            @click="getDomesticStockSearchData"
          >
            <i class="bi bi-search"></i>
          </button>
        </div>
      </div>
      <div class="contents-body table-responsive-xxl">
        <!-- 검색했냐? -->
        <div v-if="isUserSearching">
          <table class="table">
            <thead class="table-dark">
              <tr>
                <th scope="col">코드</th>
                <th scope="col">업종</th>
                <th scope="col">현재가</th>
                <th scope="col">전일 대비</th>
                <th scope="col">전일 대비율</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr v-if="stockData" @click="goDomesticStockDetail(stockCode)">
                <th scope="row">{{ stockCode }}</th>
                <td>{{ stockData.bstp_kor_isnm }}</td>
                <td>{{ stockData.stck_prpr }}</td>
                <td>{{ stockData.prdy_vrss }}</td>
                <td>{{ stockData.prdy_ctrt }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 안했으면 이거나 봐라 -->
        <div v-else class="indexes flex">
          <div class="kospi">KOSPI 줘봐라</div>
          <div class="kosdaq">KOSDAQ 줘보봐라</div>
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
    return {
      isUserSearching: true,
      stockCode: null,
      stockData: null,
    };
  },
  methods: {
    userSearch(id) {
      console.log(id);
      this.isUserSearching = !this.isUserSearching;
    },
    getDomesticStockSearchData() {
      this.$axios
        .get("/api/v1/asset/stock/domestic", {
          params: {
            stockCode: this.stockCode,
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            this.stockData = res.data.data;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    goDomesticStockDetail(stockCode) {
      this.$router.push({
        name: "PageStockDomesticDetail",
        params: { stockCode: stockCode },
      });
    },
  },
};
</script>
<style lang="scss" scoped>
body {
  background-color: black;
}
.contents {
  background-color: #fff;
  &-head {
    div {
      width: 20vw;
      height: 50%;
    }
  }
  &-body {
    width: 100%;
    height: 100%;
    .indexes {
      justify-content: space-around;
      height: 100%;
      div {
        background-color: pink;
        width: 45%;
        height: 20%;
      }
    }
  }
}
</style>
