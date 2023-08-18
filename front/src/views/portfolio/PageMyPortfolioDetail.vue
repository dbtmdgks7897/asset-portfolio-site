<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="move-buttons">
      <button
        class="btn detail-move-button1"
        @click="goSelectPortfolioPage"
      >
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
      <div v-if="data" class="contents-body grid">
        <!-- idx 가져와서 링크 받아야함 -->
        <div v-for="key in titleList" :key="key"
          class="portfolio"
        >
          <div class="portfolio-head">
            <span>{{ key }}</span>
          </div>
          <div class="portfolio-body">
            <span>
                <Pie v-if="data[key].labels.length != 0" :data="data[key]" :options="options" />
              <Pie v-else :data="tempData" :options="options" />
            </span>
          </div>
        </div>
      </div>
    </div>
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
      portfolioIdx: localStorage.getItem("portfolioIdx"),
      portfolioName: localStorage.getItem("portfolioName"),
      data: null,
      titleList: ["주식", "외화", "암호화폐"],
      tempData,
      options,
    };
  },
  mounted() {
    // login.getUserProfile();
    this.getPortfolioDetail();
  },
  components: { Pie },
  methods: {
    getPortfolioDetail(){
        this.$axios
        .get(`/api/v1/portfolio/detail/${this.portfolioIdx}`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.data = res.data.data;
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
    &:hover{
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
      background-color: gray;
      border: 1px solid black;
      box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.3);
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
}
</style>
