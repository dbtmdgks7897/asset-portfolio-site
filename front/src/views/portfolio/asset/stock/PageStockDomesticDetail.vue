<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>주식 이름</span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <!-- 검색했냐? -->
        
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
    getDomesticStockSearchData(){

      this.$axios
      .get('/api/v1/asset/stock/domestic',{
        params: {
            stockCode : this.stockCode
          },
        }).then((res) => {
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
},
}
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
