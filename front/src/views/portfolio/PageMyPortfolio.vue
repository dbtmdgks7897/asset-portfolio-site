<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>내 포트폴리오</span>
      </div>
      <div class="contents-body grid">
        <!-- idx 가져와서 링크 받아야함 -->
        <div v-for="portfolio in portfolioList" :key="portfolio" class="portfolio">
          <div class="portfolio-head"><span>(포트폴리오 이름)</span></div>
          <div class="portfolio-body"><span>
            <Pie :data="data" :options="options" />
          </span></div>
        </div>
        <div class="portfolio portfolio-add flex" @click="addPortfolioButton">
          <span class="icon"><i class="bi bi-plus-square"></i></span>
          <span> Add Portfolio </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Pie } from 'vue-chartjs'
import { data, options } from '@/utils/chartConfig'

ChartJS.register(ArcElement, Tooltip, Legend)

</script>
<script>
export default {
  data() {
    return {
        data,
        options,
        portfolioList: null,
    };
  },
  mounted() {
    console.log();
  },
  components: { Pie },
  methods :{
    addPortfolioButton () {

    }
  }

};
</script>
<style lang="scss" scoped>
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
    grid-template-columns: repeat(3, 1fr); /* 한 행에 3개의 열을 생성 */
    gap: 20px; /* 열 사이의 간격 조정 */
    .portfolio {
      width: 100%;
      height: 100%;
      padding: 5%;
      color: black;
      background-color: gray;
      border: 1px solid black;
      box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2);
      position: relative;
      transition: transform 0.3s, box-shadow 0.3s;
      &:hover {
        transform: scale(1.1);
      }

      &-head {
        font-size: 4vh;
        height: 20%;
        border-bottom: 1px solid;
        align-items: center;
      }
      &-body {
        height: 80%;
      }
    }

    .portfolio-add {
      justify-content: center;
      align-items: center;
      flex-direction: column;
      span {
        font-size: 3vh;
      }
      .icon {
        font-size: 5vh;
        transition: transform 0.3s, color 0.3s;
      }
      &:hover {
        .icon {
          transform: scale(1.1);
          color: blueviolet;
        }
      }
    }
  }
}
</style>
