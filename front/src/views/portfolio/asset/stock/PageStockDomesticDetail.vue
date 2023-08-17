<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div v-if="stockHead" class="contents-head flex">
        <span
          >{{ util.truncateText(stockHead.assetName, 12) }}
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
        <span>({{ stockHead.assetCode }})</span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <div v-if="stockData.price">
          <div class="flex pricebox">
            <p class="price">{{ stockData.price }}</p>
            <p class="compare" :style="getPriceStyle(stockData.compareYester)">
              <span>{{ stockData.compareYester }}</span
              >({{ stockData.compareYesterRate }}%)
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
        <div v-else>
          <div class="flex pricebox">
            <p class="price">{{ tempStockPrice }}</p>
          </div>
        </div>
        <div class="buttons">
          <button
            @click="purchaseModal"
            data-bs-toggle="modal"
            data-bs-target="#exampleModal"
            data-bs-whatever="@mdo"
            class="btn my-button"
          >
            <span>구매</span>
          </button>
          <button
            @click="sellModal"
            data-bs-toggle="modal"
            data-bs-target="#exampleModal"
            data-bs-whatever="@mdo"
            class="btn my-button"
          >
            <span>판매</span>
          </button>
        </div>
      </div>
    </div>
    <div
      class="modal fade"
      id="exampleModal"
      tabindex="-1"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="exampleModalLabel">
              {{ modalStatus }}
            </h1>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <form>
              <div class="mb-3">
                <label for="recipient-name" class="col-form-label"
                  >액수 조정</label
                >
                <input
                  v-model="finalPrice"
                  type="number"
                  class="form-control"
                  id="recipient-name"
                />
              </div>
              <div class="mb-3">
                <label for="recipient-name" class="col-form-label"
                  >{{ modalStatus }} 갯수 :
                </label>
                <input
                  v-model="amount"
                  type="number"
                  class="form-control"
                  id="recipient-name"
                />
              </div>
              <div class="mb-3">
                <label for="message-text" class="col-form-label"
                  >총 금액 :</label
                >
                <textarea
                  :value="result"
                  class="form-control"
                  id="message-text"
                  disabled
                >
                </textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              닫기
            </button>
            <button
              @click="buttonHandler"
              data-bs-toggle="modal"
              type="button"
              class="btn btn-primary"
            >
              {{ modalStatus }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { util } from "@/utils/utils";
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
      tempStockPrice: this.$route.query.stockPrice,
      assetType: "주식-국내",
      stockHead: null,
      stockData: {
        price: null,
        compareYester: null,
        compareYesterRate: null,
        highPrice: null,
        rowPrice: null,
      },
      modalStatus: null,
      approvalKey: null,
      amount: 1,
      result: null,
      isBookmark: null,
      finalPrice: null,
    };
  },
  mounted() {
    this.assetRegistration();
    this.connectToWebSocket();
    this.isBookmarked();
  },
  watch: {
    amount: function (val) {
      //   var temp = null;
      //   if (this.stockData.price == null) {
      //     temp = this.tempStockPrice;
      //   } else {
      //     temp = this.stockData.price;
      //   }
      this.result = this.finalPrice * val;
    },
  },
  async beforeUnmount() {
    if (this.socket) {
      // 뷰가 제거되기 전에 웹소켓을 닫습니다.
      this.socket.close();
    }
  },
  methods: {
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
    userSearch(id) {
      console.log(id);
      this.isUserSearching = !this.isUserSearching;
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
    assetRegistration() {
      this.$axios
        .get(
          `/api/v1/asset/stock/domestic/${this.stockCode}`,
          {
            params: {
              stockType: this.assetType,
            },
          },
          {
            headers: {
              "Content-Type": "application/json;charset=utf-8",
            },
          }
        )
        .then((res) => {
          if (res.data.code === 0) {
            this.stockHead = res.data.data;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    // getDomesticStockDetailData() {
    //   this.$axios
    //     .get("/api/v1/asset/stock/domestic/" + this.stockCode, {
    //       params: {
    //         stockCode: this.stockCode,
    //       },
    //     })
    //     .then((res) => {
    //       if (res.data.code === 0) {
    //         this.stockData = res.data.data;
    //       } else {
    //         alert(res.data.message);
    //       }
    //     })
    //     .catch((err) => {
    //       alert(err.response.data.message);
    //     });
    // },
    async connectToWebSocket() {
      try {
        const response = await this.$axios.post("/api/v1/apiAuth/approval");
        this.approvalKey = response.data;

        if (this.approvalKey) {
          this.socket = new WebSocket(
            `ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0?approval_key=${this.approvalKey}`
          );

          this.socket.onopen = () => {
            console.log("Connected to WebSocket");
            this.sendStockInfo();
          };

          this.socket.onmessage = async (event) => {
            const data = event.data.split("|");
            const signal = data[0];
            if (signal === "0" || signal === "1") {
              const tempStockData = data[3].split("^");
              this.stockData.price = tempStockData[2];
              this.stockData.compareYester = tempStockData[4];
              this.stockData.compareYesterRate = tempStockData[5];
              this.stockData.highPrice = tempStockData[8];
              this.stockData.rowPrice = tempStockData[9];

              console.log(tempStockData);
            }
          };

          this.socket.onerror = (error) => {
            console.error("WebSocket Error", error);
          };

          this.socket.onclose = (event) => {
            console.log("WebSocket closed", event);
          };
        }
      } catch (error) {
        console.error("Error fetching approval key:", error);
      }
    },
    sendStockInfo() {
      const message = {
        header: {
          approval_key: this.approvalKey,
          custtype: "P",
          tr_type: "1",
          "content-type": "utf-8",
        },
        body: {
          input: {
            tr_id: "H0STCNT0",
            tr_key: this.stockCode, // 이 부분은 원하는 종목 코드로 변경해야 합니다.
          },
        },
      };

      this.socket.send(JSON.stringify(message));
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
