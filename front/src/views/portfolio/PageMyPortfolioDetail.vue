<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="move-buttons">
      <button class="btn detail-move-button1" @click="goSelectPortfolioPage">
        <span>
          <i class="bi bi-caret-left-fill"></i>
          선택창
        </span>
      </button>
    </div>
    <div class="contents">
      <div class="contents-head">
        <span>{{ portfolioName }} 포트폴리오</span>
      </div>
      <div v-if="chartData" class="contents-body grid table-responsive-xxl">
        <!-- idx 가져와서 링크 받아야함 -->
        <div v-for="key in titleList" :key="key" class="portfolio">
          <div class="portfolio-head">
            <span>{{ key }}</span>
          </div>
          <div class="portfolio-body">
            <span>
              <Pie
                v-if="chartData[key].labels.length != 0"
                :data="chartData[key]"
                :options="options"
              />
              <Pie v-else :data="tempData" :options="options" />
            </span>
          </div>
        </div>
      </div>
      <div class="listLabel">
        <p class="detailListTitle">보유 자산 리스트</p>
        <button class="btn my-button manageBtn"
        @click="this.$router.push('/portfolio/manage')">관리</button>
      </div>
      <table class="table">
        <thead class="table-dark">
          <tr>
            <th scope="col">코드</th>
            <th>이름</th>
            <th>평균 구매액</th>
            <th>현재가</th>
            <th>수량</th>
            <th>총액</th>
            <th>수익</th>
          </tr>
        </thead>
        <tbody>
          <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
          <tr style="font-weight: bold;" v-for="detail in listData" :key="detail">
            <th scope="row">{{ detail.code }}</th>
            <td>{{ detail.name }}</td>
            <td>{{ detail.purchasePrice.toLocaleString() }} 원</td>
            <td>{{ detail.curPrice != null ? detail.curPrice.toLocaleString() : null }} 원</td>
            <td>{{ detail.amount }} 개</td>
            <td>{{ parseInt((detail.amount * detail.purchasePrice).toFixed(0)).toLocaleString() }} 원</td>
            <td>{{ detail.profit != null ? detail.profit.toLocaleString() : null }} 원</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="contents-body"></div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "vue-chartjs";
import { tempData, options } from "@/utils/chartConfig";

ChartJS.register(ArcElement, Tooltip, Legend);
</script>
<script>
export default {
  data() {
    return {
      portfolioIdx: sessionStorage.getItem("portfolioIdx"),
      portfolioName: sessionStorage.getItem("portfolioName"),
      chartData: null,
      listData: null,
      titleList: ["주식", "외화", "암호화폐"],
      tempData,
      options,
    };
  },
  mounted() {
    if(sessionStorage.getItem("portfolioIdx") == null){
      alert('먼저 포트폴리오를 선택해주세요.');
      this.$router.push('/portfolio');
    }
    this.getPortfolioDetailChart();
    this.getPortfolioDetailList();
  },
  components: { Pie },
  methods: {
    getPortfolioDetailChart() {
      this.$axios
        .get(`/api/v1/portfolio/detail/${this.portfolioIdx}/chart`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.chartData = res.data.data;
          } else {
            if (res.data.data != null) {
              alert(res.data.data);
            }
          }
        })
        .catch((err) => {
          if(err.response.status == 500){
            alert("로그인 해주세요")
            this.$router.push("/login")
          }
        });
    },
    getPortfolioDetailList() {
      this.$axios
        .get(`/api/v1/portfolio/detail/${this.portfolioIdx}/list`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.listData = res.data.data.detailList;
          } else {
            if (res.data.data != null) {
              alert(res.data.data);
            }
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    goSelectPortfolioPage(){
        sessionStorage.removeItem("portfolioIdx");
        sessionStorage.removeItem("portfolioName");
        this.$router.push('/portfolio')
    }
  },    
};
</script>
<style lang="scss" scoped>
.move-buttons {
  position: absolute;
  z-index: 1;
  span {
    font-size: 2vw;
  }
  button {
    padding: 0;
    opacity: 0.4;
    transition: 0.5s;
    &:hover {
      opacity: 1;
    }
  }
}
.contents {
  margin: 0;
  width: 100%;
  height: 100%;
  background-color: white;
  &-head {
    display: flex;
    height: 10%;
    margin: 0;
    span {
      font-size: 4vw;
    }
  }
  &-body {
    padding: 5%;
    height: 100%;
    grid-template-columns: repeat(3, 22vw); /* 한 행에 3개의 열을 생성 */
    gap: 5vh; /* 열 사이의 간격 조정 */
    .portfolio {
      //   width: 100%;
      height: 110%;
      min-height: 40vh;
      color: black;
      background-color: rgb(255, 255, 255);
      border: 1px solid;
      box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.1);
      position: relative;
      &-head {
        padding: 5%;
        font-size: 4vh;
        height: 20%;
        border-bottom: 1px solid;
        align-items: center;
      }
      &-body {
        height: 80%;
      }
    }
  }
  .listLabel {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .detailListTitle {
      font-size: 5vh;
    }
  }
}
</style>
