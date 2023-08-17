<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span
          >외화
          <a
            style="font-size: 2vw"
            href="https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies.json"
            target="_blank"
            >코드 검색</a
          >
        </span>

        <span class="label">출발 통화 : </span>
        <select
          style="width: 10vw"
          v-model="from"
          class="form-select form-select-sm"
          aria-label="Small select example"
        >
          <option value="krw" selected>원 (한국)</option>
          <option value="usd">달러 (미국)</option>
          <option value="jpy">엔화 (일본)</option>
          <option value="eur">유로 (유럽)</option>
          <option value="gbp">파운드 (영국)</option>
        </select>
        <span class="label">도착 통화 : </span>
        <select
          style="width: 10vw"
          v-model="to"
          class="form-select form-select-sm"
          aria-label="Small select example"
        >
          <option value="usd" selected>달러 (미국)</option>
          <option value="krw">원 (한국)</option>
          <option value="jpy">엔화 (일본)</option>
          <option value="eur">유로 (유럽)</option>
          <option value="gbp">파운드 (영국)</option>
        </select>
      </div>
      <div class="contents-body table-responsive-xxl">
        <!-- 검색했냐? -->
        <div>
          <table class="table">
            <thead class="table-dark">
              <tr>
                <th scope="col">출발 통화 ({{ from }})</th>
                <th scope="col">가격</th>
                <th scope="col">도착 통화 ({{ to }})</th>
                <th scope="col">가격</th>
                <th scope="col" v-if="from == `krw`"></th>
                <th scope="col" v-if="from == `krw`"></th>
              </tr>
            </thead>
            <tbody v-if="to == null">
              <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr v-for="(value, key) in currencyData" :key="key">
                <th scope="row">{{ from }}</th>
                <td><input type="number" v-model="fromPrice" /></td>
                <td>{{ key }}</td>
                <td>
                  {{
                    isNaN(parseFloat(value) * parseFloat(fromPrice))
                      ? 0
                      : (parseFloat(value) * parseFloat(fromPrice)).toFixed(1)
                  }}
                </td>
                <td v-if="from == `krw`">
                  <button
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                    data-bs-whatever="@mdo"
                    class="btn"
                    @click="
                      purchaseModal();
                      selectedPrice = value;
                      selected = key;
                    "
                  >
                    구매
                  </button>
                </td>
                <td v-if="from == `krw`">
                  <button
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                    data-bs-whatever="@mdo"
                    class="btn"
                    @click="
                      sellModal();
                      selectedPrice = value;
                      selected = key;
                    "
                  >
                    판매
                  </button>
                </td>
              </tr>
            </tbody>
            <tbody v-else>
              <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr>
                <th scope="row">{{ from }}</th>
                <td><input type="number" v-model="fromPrice" /></td>
                <td>{{ to }}</td>
                <td>
                  {{
                    isNaN(currencyData * parseFloat(fromPrice))
                      ? 0
                      : (currencyData * parseFloat(fromPrice)).toFixed(1)
                  }}
                </td>
                <td v-if="from == `krw`">
                  <button
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                    data-bs-whatever="@mdo"
                    class="btn"
                    @click="
                      purchaseModal();
                      selectedPrice = currencyData;
                      selected = to;
                    "
                  >
                    구매
                  </button>
                </td>
                <td v-if="from == `krw`">
                  <button
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                    data-bs-whatever="@mdo"
                    class="btn"
                    @click="
                      sellModal();
                      selectedPrice = currencyData;
                      selected = to;
                    "
                  >
                    판매
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
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
        <div v-if="modalStatus === `구입`" class="modal-body">
          <form>
            <div class="mb-3">
              <label for="recipient-name" class="col-form-label"
                >구매가 조정</label
              >
              <input
                v-model="selectedPrice"
                type="number"
                class="form-control"
                id="recipient-name"
              />
            </div>
            <div class="mb-3">
              <label for="recipient-name" class="col-form-label"
                >시작 통화 (krw):</label
              >
              <input
                v-model="krwPrice"
                type="number"
                class="form-control"
                id="recipient-name"
              />
            </div>
            <div class="mb-3">
              <label for="message-text" class="col-form-label"
                >도착 통화({{ selected }}) :</label
              >
              <textarea
                v-model="result"
                class="form-control"
                id="message-text"
                disabled
              >
              </textarea>
            </div>
          </form>
        </div>
        <div v-if="modalStatus === `판매`" class="modal-body">
          <form>
            <div class="mb-3">
              <label for="recipient-name" class="col-form-label"
                >판매가 조정</label
              >
              <input
                v-model="selectedPrice"
                type="number"
                class="form-control"
                id="recipient-name"
              />
            </div>
            <div class="mb-3">
              <label for="recipient-name" class="col-form-label"
                >시작 통화 (krw):</label
              >
              <input
                v-model="result"
                type="number"
                class="form-control"
                id="recipient-name"
                disabled
              />
            </div>
            <div class="mb-3">
              <label for="message-text" class="col-form-label"
                >도착 통화({{ selected }}) :</label
              >
              <textarea
                v-model="toPrice"
                class="form-control"
                id="message-text"
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
</template>

<script setup>
import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  data() {
    //변수생성
    return {
      from: "krw",
      fromPrice: 1000,
      toPrice: 0,
      result: null,
      to: null,
      currencyData: null,
      selected: null,
      krwPrice: null,
      selectedPrice: null,
      modalStatus: null,
    };
  },
  mounted() {
    this.getCurreny();
  },
  watch: {
    from: function (val) {
      console.log(val);
      this.$axios
        .get(
          `https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/${val}.json`
        )
        .then((res) => {
          this.currencyData = res.data[val];
          console.log(res.data);
        });
    },
    to: function (val) {
      this.$axios
        .get(
          `https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/${this.from}/${val}.json`
        )
        .then((res) => {
          this.currencyData = res.data[val];
          console.log(res);
        });
    },
    fromPrice: function (val) {
      this.fromPrice = val;
    },
    krwPrice: function (val) {
      this.result = (parseFloat(val) * parseFloat(this.selectedPrice)).toFixed(
        1
      );
    },
    toPrice: function (val) {
      this.result = (parseFloat(val) / parseFloat(this.selectedPrice)).toFixed(
        1
      );
    },
  },
  methods: {
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
    },
    purchaseButton() {
      if (
        confirm(
          `${this.selected}를 ${(
            parseFloat(this.krwPrice) * parseFloat(this.selectedPrice)
          ).toFixed(1)}만큼 구입하시겠습니까?`
        )
      ) {
        const data = {
          asset: {
            idx: `${this.selected.toUpperCase()}/KRW`,
            name: this.selected,
            type: "외화",
          },
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.result,
            averagePurchasePrice: this.krwPrice / this.result,
            totalPurchasePrice: this.krwPrice,
          },
          transaction: {
            type: "구입",
            amount: parseFloat(this.result),
            priceAvg: 1 / this.selectedPrice,
            profit: this.krwPrice * -1,
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
    },
    sellButton() {
      if (
        confirm(
          `${this.selected}를 ${parseFloat(this.toPrice).toFixed(
            1
          )}만큼 판매하시겠습니까?`
        )
      ) {
        const data = {
          assetIdx: `${this.selected.toUpperCase()}/KRW`,
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.toPrice,
            totalSellPrice: this.result,
          },
          transaction: {
            type: "판매",
            amount: this.toPrice,
            priceAvg: 1 / this.selectedPrice,
            profit: this.krwPrice,
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
    getCurreny() {
      this.$axios
        .get(
          `https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/krw.json`
        )
        .then((res) => {
          this.currencyData = res.data.krw;
          console.log(res.data.krw);
        })
        .catch((err) => {
          console.error(err);
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
  height: 100%;
  .label {
    font-size: 1vw;
    margin-left: auto;
  }
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

.btn {
  width: 5vw;
}

.bookmark {
  z-index: 1;
}
</style>
