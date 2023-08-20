<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>{{ portfolioName }} 분석</span>
        <!-- <button v-if="!onlyProfit" class="btn" @click="onlyProfit = true">
          수익만 보기
        </button> -->
        <button v-if="onlyProfit" class="btn" @click="onlyProfit = false">
          전체 보기
        </button>
      </div>
      <div v-if="analData" class="contents-body grid">
        <div
          v-for="(value, key) in analTypeList"
          :key="key"
          class="analyze-for grid"
        >
          <div
            class="analyze-for-in-for"
            v-for="(inVal, index) in analData[key]"
            :key="inVal"
          >
            <div
              class="analyze-box-container"
            >
              <div class="analyze-box">
                <span>{{ key }} {{ analCategoryList[index] }}</span>
                <span class="price">{{ inVal.toLocaleString() }} {{ index != 3 ? '원' : '%' }}</span>
              </div>
              <div class="overlap-div" :class="value.numClass">
                <span><i class="bi" :class="value.iClass"></i></span>
              </div>
            </div>
          </div>
          <!-- <div class="analyze-box-container">
            <div class="analyze-box">
              <span>{{ analType }} 평가액</span>
              <span class="price">{{ analData[key] }}</span>
            </div>
            <div class="overlap-div first">
              <span><i class="bi bi-cash"></i></span>
            </div>
          </div>
          <div class="analyze-box-container">
            <div class="analyze-box">
                <span>{{ analType }} 수익금</span>
              <span class="price">{{ analData[key] }}</span>
            </div>
            <div class="overlap-div first">
              <span><i class="bi bi-cash"></i></span>
            </div>
          </div>
          <div class="analyze-box-container">
            <div class="analyze-box">
                <span>{{ analType }} 수익률</span>
              <span class="price">{{ analData[analType] }}</span>
            </div>
            <div class="overlap-div first">
              <span><i class="bi bi-cash"></i></span>
            </div>
          </div> -->
        </div>
        <!-- <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[1] }} 투자액</span>
            <span class="price">{{ analData[analTypeList[1]][0] }}</span></div>
          <div class="overlap-div second">
            <span><i class="bi bi-graph-up"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[1] }} 평가액</span>
            <span class="price">{{ analData[analTypeList[1]][1] }}</span></div>
          <div class="overlap-div second">
            <span><i class="bi bi-graph-up"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[1] }} 수익금</span>
            <span class="price">{{ analData[analTypeList[1]][2] }}</span></div>
          <div class="overlap-div second">
            <span><i class="bi bi-graph-up"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[1] }} 수익률</span>
            <span class="price">{{ analData[analTypeList[1]][3] }}</span></div>
          <div class="overlap-div second">
            <span><i class="bi bi-graph-up"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[2] }} 투자액</span>
            <span class="price">{{ analData[analTypeList[2]][0] }}</span></div>
          <div class="overlap-div third">
            <span><i class="bi bi-currency-dollar"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[2] }} 평가액</span>
            <span class="price">{{ analData[analTypeList[2]][1] }}</span></div>
          <div class="overlap-div third">
            <span><i class="bi bi-currency-dollar"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[2] }} 수익금</span>
            <span class="price">{{ analData[analTypeList[2]][2] }}</span></div>
          <div class="overlap-div third">
            <span><i class="bi bi-currency-dollar"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[2] }} 수익률</span>
            <span class="price">{{ analData[analTypeList[2]][3] }}</span></div>
          <div class="overlap-div third">
            <span><i class="bi bi-currency-dollar"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[3] }} 투자액</span>
            <span class="price">{{ analData[analTypeList[3]][0] }}</span></div>
          <div class="overlap-div fourth">
            <span><i class="bi bi-currency-bitcoin"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[3] }} 평가액</span>
            <span class="price">{{ analData[analTypeList[3]][1] }}</span></div>
          <div class="overlap-div fourth">
            <span><i class="bi bi-currency-bitcoin"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[3] }} 수익금</span>
            <span class="price">{{ analData[analTypeList[3]][2] }}</span></div>
          <div class="overlap-div fourth">
            <span><i class="bi bi-currency-bitcoin"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[3] }} 수익률</span>
            <span class="price">{{ analData[analTypeList[3]][3] }}</span></div>
          <div class="overlap-div fourth">
            <span><i class="bi bi-currency-bitcoin"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[4] }} 투자액</span>
            <span class="price">{{ analData[analTypeList[4]][0] }}</span></div>
          <div class="overlap-div fifth">
            <span><i class="bi bi-file-plus"></i></span>
          </div>
        </div>
        <div v-if="!onlyProfit" class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[4] }} 평가액</span>
            <span class="price">{{ analData[analTypeList[4]][0] }}</span></div>
          <div class="overlap-div fifth">
            <span><i class="bi bi-file-plus"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[4] }} 수익금</span>
            <span class="price">{{ analData[analTypeList[4]][0] }}</span></div>
          <div class="overlap-div fifth">
            <span><i class="bi bi-file-plus"></i></span>
          </div>
        </div>
        <div class="analyze-box-container">
          <div class="analyze-box"><span>{{ analTypeList[4] }} 수익률</span>
            <span class="price">{{ analData[analTypeList[4]][0] }}</span></div>
          <div class="overlap-div fifth">
            <span><i class="bi bi-file-plus"></i></span>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
// import { Pie } from "vue-chartjs";
// import { tempData, options } from "@/utils/chartConfig";

ChartJS.register(ArcElement, Tooltip, Legend);
</script>
<script>
export default {
  data() {
    return {
      //   options,
      portfolioIdx: sessionStorage.getItem("portfolioIdx"),
      portfolioName: sessionStorage.getItem("portfolioName"),
      portfolioDescription: "설명 없음",
      onlyProfit: false,
      //   analTypeList: ["총", "주식", "외화", "암호화폐"],
      analTypeList: {
        총: {
          numClass: "first",
          iClass: "bi-cash",
        },
        주식: {
          numClass: "second",
          iClass: "bi-graph-up",
        },
        외화: {
          numClass: "third",
          iClass: "bi-currency-dollar",
        },
        암호화폐: {
          numClass: "fourth",
          iClass: "bi-currency-bitcoin",
        },
      },
      analCategoryList:['투자액', '평가액', '수익금', '수익률'],
      analData: null,
      //   invisiableClass:['not-profit', 'not-profit', '', '']
    };
  },
  mounted() {
    if (sessionStorage.getItem("portfolioIdx") == null) {
      alert("먼저 포트폴리오를 선택해주세요.");
      this.$router.push("/portfolio");
    }
    this.getAnalyzeData();
  },
  //   components: { Pie },
  methods: {
    getAnalyzeData() {
      this.$axios
        .get(`/api/v1/portfolio/analyze/${this.portfolioIdx}`)
        .then((res) => {
          if (res.data.code === 0) {
            this.analData = res.data.data;
            console.log(this.analData);
          } else {
            if (res.data.data != null) {
              console.log(res.data.data);
            }
          }
        })
        .catch((err) => {
          if (err.response.status == 500) {
            alert("로그인 해주세요");
            console.log(err);
            // this.$router.push("/login")
          }
        });
    },
    isOnlyProfit(idx) {
      if (this.onlyProfit) {
        if (idx === 0 || idx === 1) {
          return true;
        }
      }
      return false;
    },
  },
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
    .analyze-for {
      padding-top: 2%;
      height: 100%;
      grid-template-columns: repeat(4, 22%); /* 한 행에 3개의 열을 생성 */
      gap: 5vh;
      .analyze-box-container {
        width: 100%;
        height: 20%;
        position: relative;
        display: inline-block; /* 컨테이너를 인라인 블록으로 배치 */
        .analyze-box {
          display: flex;
          flex-direction: column;
          width: 100%;
          padding: 2%;
          height: 11vh;
          background-color: rgb(68, 68, 68);
          border-radius: 5px;
          box-shadow: 2px 4px 10px rgba(0, 0, 0, 0.5);
          color: rgb(180, 180, 180);
          font-size: 1vw;
          .price {
            font-size: 2vw;
            margin-top: 1vh;
            color: white;
          }
        }
        .overlap-div {
          display: flex;
          align-items: center;
          justify-content: center;
          position: absolute;
          right: -10px; /* 살짝 우측으로 이동 */
          top: -10px;
          width: 4vw; /* 적절한 너비 조절 */
          height: 4vw;
          border-radius: 5px;
          background-color: rgb(255, 255, 255); /* 살짝 투명한 배경 색 */
          span {
            color: white;
            font-size: 2vw;
          }
        }

        .first {
          $first-color: rgb(0, 0, 0);
          background-color: $first-color;
          box-shadow: 0px 0px 10px $first-color;
        }
        .second {
          $first-color: #cd1818;
          background-color: $first-color;
          box-shadow: 0px 0px 10px $first-color;
        }
        .third {
          $first-color: rgb(77, 218, 65);
          background-color: $first-color;
          box-shadow: 0px 0px 10px $first-color;
        }
        .fourth {
          $first-color: #b8621b;
          background-color: $first-color;
          box-shadow: 0px 0px 10px $first-color;
        }
        .fifth {
          $first-color: #262a56;
          background-color: $first-color;
          box-shadow: 0px 0px 10px $first-color;
        }
      }
    }
  }
}
</style>
