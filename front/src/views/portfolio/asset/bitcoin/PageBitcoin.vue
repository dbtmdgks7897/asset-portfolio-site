<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>암호화폐 </span>
        <div class="input-group mb-1">
          <input
            type="text"
            v-model="searchBitcoinCode"
            class="form-control"
            placeholder="종목 코드로만 검색 가능"
            aria-label="검색어"
            aria-describedby="button-addon2"
            @keydown.enter="getBitcoinSearchData"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/boards 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
            @click="getBitcoinSearchData"
          >
            <i class="bi bi-search"></i>
          </button>
        </div>
      </div>

      <div class="contents-body table-responsive-xxl">
        <div>
          <table class="table">
            <thead class="table-dark">
              <tr>
                <th scope="col">코드</th>
                <th scope="col">종목명</th>
              </tr>
            </thead>
            <tbody>
              <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr @click="this.$router.push({
                  name: 'PageBitcoinDetail',
                  params: { bitcoinCode: bitcoin.market },
                  query: { bitcoinName: bitcoin.korean_name },
                })" v-for="bitcoin in bitcoinData" :key="bitcoin">
                <th v-if="bitcoin.market.startsWith('KRW')" scope="row">
                  {{ bitcoin.market }}
                </th>
                <td v-if="bitcoin.market.startsWith('KRW')">
                  {{ bitcoin.korean_name }}
                </td>
              </tr>
            </tbody>
          </table>
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
      bitcoinData: null,
      modalStatus: null,
      searchBitcoinCode: null,
    };
  },
  mounted() {
    this.getBitcoinList();
  },
  watch: {
    searchBitcoinCode: function (val) {
      this.bitcoinData = this.bitcoinData.filter((item) => {
        return (
          item.market.includes(val) ||
          item.korean_name.includes(val)
        );
      });
      if(val == ""){
        this.getBitcoinList();
      }
    },
  },
  methods: {
    getBitcoinList() {
      this.$axios
        .get(`https://api.upbit.com/v1/market/all`)
        .then((res) => {
          this.bitcoinData = res.data;
        })
        .catch((err) => {
          if(err.response.status == 500){
            alert("로그인 해주세요")
            this.$router.push("/login")
          }
        });
    },
    getBitcoinSearchData() {},
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
