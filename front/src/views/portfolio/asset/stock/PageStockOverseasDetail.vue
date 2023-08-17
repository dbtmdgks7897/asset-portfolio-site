<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span
          >{{ stockCode }}
          <a
            ><i
              v-show="isBookmark"
              @click="bookmarking"
              class="bi bi-star-fill"
            ></i>
          </a>
          <a
            ><i v-show="!isBookmark" @click="bookmarking" class="bi bi-star"></i
          ></a>
        </span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <div>
          <div class="flex pricebox">
            <p class="price">{{ stockData.price }}</p>
            <p class="compare" :style="getPriceStyle(stockData.compareYester)">
              <span>{{ stockData.compareYester }}</span
              >
            </p>
          </div>
          <div class="pricebox downside">
            <p style="color: red">
              <span>금일 최고가</span> | {{ stockData.highPrice }}
            </p>
            <p style="color: blue">
              <span>금일 최저가</span> | {{ stockData.rowPrice }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
// import { util } from "@/utils/utils";
</script>
<script>
// /*** Start	unicode replace step ********************************************************/
// // eslint-disable-next-line no-control-regex
// var escapable = /[\x00-\x1f\ud800-\udfff\u200c\u2028-\u202f\u2060-\u206f\ufff0-\uffff]/g;
// function filterUnicode(quoted)	{
// 	escapable.lastIndex = 0;
// 	if (!escapable.test(quoted))	return quoted;

// 	return quoted.replace (escapable, function(){
// 		return '';
// 	});
// }
// /** End of unicode replace step ********************************************************/

export default {
  data() {
    //변수생성
    return {
      /** @type {WebSocket} */
      socket: null,
      stockCode: this.$route.params.stockCode,
      assetType: "주식_해외",
      stockData: {
        price: null,
        highPrice: null,
        rowPrice: null,
        compareYester: null,
        compareYesterRate: null,
      },
      modalStatus: null,
      approvalKey: null,
      amount: 1,
      result: null,
      isBookmark: null,
      finalPrice: null,
      apikey: null,
    };
  },
  mounted() {
    this.isBookmarked();
    this.getOverseasStockDetailData();
  },
  watch: {
    amount: function (val) {
      this.result = this.finalPrice * val;
    },
  },
  methods: {
    getOverseasStockDetailData() {
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
          const preDate = Object.keys(timeSeries)[1]; // 가장 최근 날짜를 추출
          const lastData = timeSeries[lastDate]; // 최근 날짜의 데이터를 추출
          const preData = timeSeries[preDate]; // 최근 날짜의 데이터를 추출

          this.stockCode = symbol;
          this.stockData = {
            highPrice: lastData["2. high"],
            rowPrice: lastData["3. low"],
            price: lastData["4. close"],
            compareYester: (parseFloat(lastData["4. close"]) - parseFloat(preData["4. close"])).toFixed(2),
            compareYesterRate: Math.abs(this.compareYester) / parseFloat(preData["4. close"]),
          };

        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
    isBookmarked() {
      this.$axios
        .get(`/api/v1/bookmark`, {
          params: {
            assetCode: this.stockCode,
          },
        })
        .then((res) => {
          this.isBookmark = res.data;
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    bookmarking() {
      const dto = {
        assetIdx: this.stockCode,
      };
      this.$axios
        .post(`/api/v1/bookmark`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.$router.go(0);
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    getPriceStyle(value) {
      const numValue = parseFloat(value);
      const style = {};

      if (numValue > 0) {
        style.color = "red"; // 양수일 경우 초록색 글자
      } else if (numValue < 0) {
        style.color = "blue"; // 음수일 경우 빨간색 글자
      } else {
        style.color = "black"; // 0일 경우 검정색 글자
      }
      return style;
    },
    buttonHandler() {
      if (this.modalStatus == "구입") {
        this.purchaseButton();
      } else if (this.modalStatus == "판매") {
        this.sellButton();
      } else {
        alert("뭐임?");
      }
    },
    purchaseModal() {
      this.modalStatus = "구입";
      this.finalPrice =
        this.stockData.price == null
          ? this.tempStockPrice
          : this.stockData.price;
    },
    purchaseButton() {
      if (
        confirm(
          `${this.stockHead.assetName}을(를) ${this.amount}만큼 구입하시겠습니까?`
        )
      ) {
        const data = {
          asset: {
            idx: this.stockCode,
            name: this.stockHead.assetName,
            type: this.assetType,
          },
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.amount,
            averagePurchasePrice: this.finalPrice,
            totalPurchasePrice: this.result,
          },
          transaction: {
            type: "구입",
            amount: this.amount,
            priceAvg: this.finalPrice,
            profit: this.result * -1,
          },
        };

        this.$axios
          .post(`/api/v1/asset/purchase`, data, {
            headers: {
              "Content-Type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              alert(res.data.message);
            } else {
              alert(res.data.message);
            }
          })
          .catch((err) => {
            alert(err.response.data.message);
          });
      }
    },
    sellModal() {
      this.modalStatus = "판매";
      this.finalPrice =
        this.stockData.price == null
          ? this.tempStockPrice
          : this.stockData.price;
    },
    sellButton() {
      if (
        confirm(
          `${this.stockHead.assetName}을(를) ${this.amount}만큼 판매하시겠습니까?`
        )
      ) {
        const data = {
          assetIdx: this.stockCode,
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.amount,
            totalSellPrice: this.result,
          },
          transaction: {
            type: "판매",
            amount: this.amount,
            priceAvg: this.finalPrice,
            profit: this.result,
          },
        };

        this.$axios
          .post(`/api/v1/asset/sell`, data, {
            headers: {
              "Content-Type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              alert(res.data.message);
            } else {
              alert(res.data.message);
            }
          })
          .catch((err) => {
            alert(err.response.data.message);
          });
      }
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
  height: 100%;
  &-head {
    span {
      font-size: 3vw;
      a {
        i {
          color: black; /* 기본 색상 */
          transition: color 0.3s;
        }
        &:hover {
          i {
            color: yellow; /* 마우스 호버 시 색상 변경 */
          }
        }
      }
    }
    div {
      width: 20vw;
      height: 50%;
    }
  }
  &-body {
    width: 100%;
    height: 70%;
    .pricebox {
      align-items: center;
      .price {
        font-size: 4vw;
      }
      p {
        font-size: 2vw;
        margin-left: 20px;
      }
    }
    .downside {
      display: flex;
      flex-direction: column;
      align-items: baseline;
    }
    .buttons {
      button {
        width: 50%;
        transition: background-color 0.3s;
        &:hover {
          &:first-child {
            background-color: lightblue;
          }

          &:last-child {
            background-color: lightcoral;
          }
        }
      }
    }
  }
}
</style>
