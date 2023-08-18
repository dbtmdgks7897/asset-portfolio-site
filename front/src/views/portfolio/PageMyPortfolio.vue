<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>내 포트폴리오</span>
      </div>
      <div class="contents-body grid">
        <!-- idx 가져와서 링크 받아야함 -->
        <div
          v-for="portfolio in portfolioList"
          :key="portfolio"
          class="portfolio"
          @click="selectPortfolio(portfolio.idx, portfolio.name)"
        >
          <div class="portfolio-head">
            <span>{{ portfolio.name }}</span>
          </div>
          <div class="portfolio-body">
            <span>
              <Pie v-if="portfolio.chartDataDTO" :data="portfolio.chartDataDTO" :options="options" />
              <Pie v-else :data="tempData" :options="options" />
            </span>
          </div>
        </div>
        <div
          class="portfolio portfolio-add flex"
          data-bs-toggle="modal"
          data-bs-target="#exampleModal"
          data-bs-whatever="@mdo"
        >
          <span class="icon"><i class="bi bi-plus-square"></i></span>
          <span> Add Portfolio </span>
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
          <h1 class="modal-title fs-5" id="exampleModalLabel">New message</h1>
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
                >포트폴리오 이름 :</label
              >
              <input
                v-model="portfolioName"
                type="text"
                class="form-control"
                id="recipient-name"
              />
            </div>
            <div class="mb-3">
              <label for="message-text" class="col-form-label">추가 설명</label>
              <textarea
                v-model="portfolioDescription"
                class="form-control"
                id="message-text"
              ></textarea>
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
            @click="addPortfolioButton"
            data-bs-toggle="modal"
            type="button"
            class="btn btn-primary"
          >
            추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "vue-chartjs";
import { data, tempData, options } from "@/utils/chartConfig";

ChartJS.register(ArcElement, Tooltip, Legend);
</script>
<script>
export default {
  data() {
    return {
      data,
      dtodata: null,
      options,
      portfolioList: null,
      portfolioName: null,
      portfolioDescription: "설명 없음",
    };
  },
  mounted() {
    // login.getUserProfile();
    this.getPortfolioList();
  },
  components: { Pie },
  methods: {
    getPortfolioList() {
      this.$axios
        .get(`/api/v1/portfolio`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data.portfolioList);
            this.portfolioList = res.data.data.portfolioList;
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
    addPortfolioButton() {
      const dto = {
        name: this.portfolioName,
        description: this.portfolioDescription,
      };
      this.$axios
        .post(`/api/v1/portfolio`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            this.getPortfolioList();
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    selectPortfolio(idx, name) {
      localStorage.setItem("portfolioIdx", idx);
      localStorage.setItem("portfolioName", name);
      this.$router.push({
        name: "PageMyPortfolioDetail",
        params: { idx: idx },
      });
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
      transition: transform 0.3s, box-shadow 0.3s;
      &:hover {
        transform: scale(1.1);
      }
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
