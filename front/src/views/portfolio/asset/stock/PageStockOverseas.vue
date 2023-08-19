<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span
          >해외 주식
          <a
            style="font-size: 2vw"
            href="https://kr.investing.com/stock-screener/?sp=country::11|sector::a|industry::a|equityType::a%3Ceq_market_cap;1"
            target="_blank"
            >종목 코드 검색</a
          >
        </span>

        <div class="input-group mb-1">
          <input
            type="text"
            v-model="stockCode"
            class="form-control"
            placeholder="종목 코드로만 검색 가능"
            aria-label="검색어"
            aria-describedby="button-addon2"
            @keydown.enter="getOverseasStockSearchData"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/boards 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
            @click="getOverseasStockSearchData"
          >
            <i class="bi bi-search"></i>
          </button>
        </div>
      </div>
      <div class="contents-body table-responsive-xxl">
        <!-- 검색했냐? -->
        <div>
          <table class="table">
            <thead class="table-dark">
              <tr>
                <th scope="col">코드</th>
                <th scope="col">종가(현재가)</th>
                <th scope="col">시작가</th>
                <th scope="col">갱신 일자</th>
              </tr>
            </thead>
            <tbody>
              <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr v-if="stockData">
                <th @click="goOverseasStockDetail(stockCode)" scope="row">
                  {{ stockCode }}
                </th>
                <td @click="goOverseasStockDetail(stockCode)">
                  {{ stockData.endPrice }}
                </td>
                <td @click="goOverseasStockDetail(stockCode)">
                  {{ stockData.startPrice }}
                </td>
                <td @click="goOverseasStockDetail(stockCode)">
                  {{ stockData.date }}
                </td>
              </tr>
            </tbody>
          </table>
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
      stockCode: null,
      stockData: null,
      dto: null,
      apikey: null,
    };
  },
  mounted() {
    this.getApikey();
  },
  methods: {
    getApikey() {
      this.$axios
        .get("/api/v1/asset/stock/overseas/apikey")
        .then((res) => {
          this.apikey = res.data;
        })
        .catch((err) => {
          if(err.response.status == 500){
            alert("로그인 해주세요")
            this.$router.push("/login")
          }
        });
    },
    userSearch(id) {
      console.log(id);
      this.isUserSearching = !this.isUserSearching;
    },
    getOverseasStockSearchData() {
      this.$axios
        .get("/api/v1/asset/stock/overseas/apikey")
        .then((res) => {
          this.apikey = res.data;
        })
        .catch((err) => {
          alert(err.response.data.message);
        });

      const apiUrl = `https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=${this.stockCode}&apikey=${this.apikey}`;
      this.$axios
        .get(apiUrl)
        .then((res) => {
          // API 응답 데이터를 responseData에 저장
          const data = res.data;
          const symbol = data["Meta Data"]["2. Symbol"];

          // 'Time Series (Daily)'에서 마지막 데이터 추출
          const timeSeries = data["Time Series (Daily)"];
          const lastDate = Object.keys(timeSeries)[0]; // 가장 최근 날짜를 추출
          const lastData = timeSeries[lastDate]; // 최근 날짜의 데이터를 추출

          this.stockCode = symbol;
          this.stockData = {
            date: lastDate,
            startPrice: lastData["1. open"],
            endPrice: lastData["4. close"],
          };
          this.dto = {
            assetCode: this.stockCode,
            assetName: symbol,
            assetType: "주식_해외",
          };
          console.log(this.stockData);
          console.log(this.dto);
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
    goOverseasStockDetail(stockCode) {
      this.$axios
        .post(`/api/v1/asset`, this.dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.message);
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
      this.$router.push({
        name: "PageStockOverseasDetail",
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
    span {
      font-size: 3vw;
    }
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

.bookmark {
  z-index: 1;
}
</style>
