<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head">
        <span>기타 자산</span>
      </div>
      <div class="contents-body grid">
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">코드</th>
              <th>이름</th>
              <th>타입</th>
              <th>가격</th>
              <th>양</th>
              <th>총액</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr>
              <th scope="row"><input type="text" oninput= "this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" v-model="code" placeholder="숫자만 입력"/></th>
              <td><input type="text" v-model="name" /></td>
              <td>
                <select
                  v-model="type"
                  class="form-select form-select-sm"
                  aria-label="Small select example"
                >
                  <option
                    v-for="assetType in assetTypeList"
                    :key="assetType"
                    :value="assetType.name"
                  >
                    {{ assetType.name }}
                  </option>
                </select>
              </td>
              <td>
                <input style="width: 6vw" type="number" v-model="price" />
              </td>
              <td><input type="number" v-model="amount" /></td>
              <td>
                <input
                  style="width: 10vw"
                  type="number"
                  v-model="totalPrice"
                  disabled
                />
              </td>
              <td>
                <button
                  data-bs-toggle="modal"
                  data-bs-target="#exampleModal"
                  data-bs-whatever="@mdo"
                  class="btn"
                  @click="purchaseButton"
                >
                  구매
                </button>
              </td>
              <td>
                <button
                  data-bs-toggle="modal"
                  data-bs-target="#exampleModal"
                  data-bs-whatever="@mdo"
                  class="btn"
                  @click="sellButton"
                >
                  판매
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p class="asset-list-title">기존 자산 목록</p>
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">코드</th>
              <th>이름</th>
              <th>타입</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr v-for="asset in assetList" :key="asset">
              <th scope="row">{{ asset.assetCode }}</th>
              <td>{{ asset.assetName }}</td>
              <td>{{ asset.assetType.name }}</td>
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
      code: null,
      name: null,
      type: null,
      price: null,
      amount: null,
      totalPrice: null,
      modalStatus: null,
      assetTypeList: null,
      assetList: null,
    };
  },
  watch: {
    amount: function (val) {
      this.totalPrice = val * this.price;
    },
  },
  mounted() {
    this.getAssetTypeList();
    this.getAssetList();
  },
  methods: {
    getAssetTypeList() {
      this.$axios
        .get(`/api/v1/asset/type`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data);
            this.assetTypeList = res.data.data.assetTypeList;
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
    getAssetList() {
        this.$axios
        .get(`/api/v1/asset`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data);
            this.assetList = res.data.data.assetList;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
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
    },
    purchaseButton() {
      if (confirm(`${this.name}를 ${this.amount}만큼 구입하시겠습니까?`)) {
        const data = {
          asset: {
            idx: this.code,
            name: this.name,
            type: this.type,
          },
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.amount,
            averagePurchasePrice: this.price,
            totalPurchasePrice: this.totalPrice,
          },
          transaction: {
            type: "구입",
            amount: this.amount,
            priceAvg: this.price,
            profit: this.totalPrice * -1,
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
        confirm(`${this.name}를 ${this.amount}만큼 구입하시겠습니까?`)
      ) {
        const data = {
          assetIdx: this.code,
          portfolioDetail: {
            portfolioIdx: localStorage.getItem("portfolioIdx"),
            amount: this.amount,
            totalSellPrice: this.totalPrice,
          },
          transaction: {
            type: "판매",
            amount: this.amount,
            priceAvg: this.price,
            profit: this.totalPrice,
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
.contents {
  input {
    width: 5vw;
  }
  select {
    width: 5vw;
  }
}
.asset-list-title{
    font-weight: bold;
    font-size: 3vh;
}
</style>
