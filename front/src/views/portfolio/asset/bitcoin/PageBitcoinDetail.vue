<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span
          ><a
            ><i
              v-show="isBookmark"
              @click="bookmarking"
              class="bi bi-star-fill"
            ></i>
          </a>
          <a
            ><i v-show="!isBookmark" @click="bookmarking" class="bi bi-star"></i
          ></a>
          {{ bitcoinName }}
        </span>
        <span>({{ bitcoinCode }})</span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <div v-if="bitcoinData">
          <div>
            <div class="flex pricebox">
              <div class="flex">
                <span class="label">현재가</span>
                <p class="price">
                  {{ bitcoinData.trade_price.toLocaleString() }}원
                </p>
              </div>
              <div class="flex">
                <span class="label">전일 대비</span>
                <p
                  class="compare"
                  :style="getPriceStyle(bitcoinData.signed_change_price)"
                >
                  <span
                    >{{
                      (bitcoinData.signed_change_price > 0 ? "+" : "") +
                      bitcoinData.signed_change_price.toLocaleString()
                    }}원</span
                  >({{ bitcoinData.signed_change_rate.toLocaleString() }}%)
                </p>
              </div>
            </div>
            <div class="pricebox downside">
              <p style="color: red">
                <span>금일 최고가</span> |
                {{ bitcoinData.high_price.toLocaleString() }}
              </p>
              <p style="color: blue">
                <span>금일 최저가</span> |
                {{ bitcoinData.low_price.toLocaleString() }}
              </p>
            </div>
            <div></div>
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
      bitcoinCode: this.$route.params.bitcoinCode,
      bitcoinName: this.$route.query.bitcoinName,
      bitcoinData: null,
      isBookmark: null,
      modalStatus: null,
      finalPrice: null,
      amount: null,
      result: null,
      assetType: "암호화폐",
    };
  },
  mounted() {
    this.getBitcoinDetailData();
    this.isBookmarked();
  },
  watch: {
    amount: function (val) {
      this.result = this.finalPrice * val;
    },
  },
  methods: {
    getBitcoinDetailData() {
      this.$axios
        .get(`/api/v1/asset/bitcoin/${this.bitcoinCode}`, {
          params: {
            bitcoinName: this.bitcoinName,
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            this.bitcoinData = res.data.data[0];
            console.log(this.bitcoinData);
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
    isBookmarked() {
      this.$axios
        .get(`/api/v1/bookmark/isBookmarked`, {
          params: {
            assetCode: this.bitcoinCode,
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
        assetIdx: this.bitcoinCode,
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
      this.finalPrice = this.bitcoinData.trade_price;
    },
    purchaseButton() {
      if (
        confirm(
          `${this.bitcoinName}을(를) ${this.amount}만큼 구입하시겠습니까?`
        )
      ) {
        const data = {
          asset: {
            idx: this.bitcoinCode,
            name: this.bitcoinName,
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
      this.finalPrice = this.bitcoinData.trade_price;
    },
    sellButton() {
      if (
        confirm(
          `${this.bitcoinName}을(를) ${this.amount}만큼 판매하시겠습니까?`
        )
      ) {
        const data = {
          assetIdx: this.bitcoinCode,
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
      justify-content: space-around;
      div {
        flex-direction: column;
        align-items: flex-start;
      }
      .price {
        font-size: 4vw;
      }
      p {
        font-size: 2vw;
        margin-left: 20px;
      }
    }
    .label {
      font-weight: bold;
      font-size: 1vw;
      color: rgb(116, 116, 116);
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
