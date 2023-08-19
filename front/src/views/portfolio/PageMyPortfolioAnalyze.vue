<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>{{ portfolioName }} 분석</span>
      </div>
      <div class="contents-body grid">

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
      portfolioList: localStorage.getItem("portfolioList"),
      portfolioName: localStorage.getItem("portfolioName"),
      portfolioDescription: "설명 없음",
    };
  },
  mounted() {
    // login.getUserProfile();
    this.getPortfolioList();
  },
//   components: { Pie },
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
          if(err.response.status == 500){
            alert("로그인 해주세요")
            this.$router.push("/login")
          }
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
    deletePortfolio(idx, name) {
      if (
        prompt(
          `정말 삭제하시겠습니까?\n삭제하시려면 아래의 이름을 입력해주세요.\n${name}`
        ) == name
      ) {
        this.$axios
          .delete(`/api/v1/portfolio/${parseInt(idx)}`)
          .then((res) => {
            if (res.data.code === 0) {
              alert("포트폴리오가 삭제되었습니다.");
             this.$router.go(0)
            } else {
              alert(res.data.message);
            }
          })
          .catch((err) => {
            alert(err.reponse);
          });
      } else{
        alert('입력값이 잘못되었습니다.')
      }
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
      background-color: rgb(202, 202, 202);
      //   border: 1px solid black;
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
        justify-content: space-between;
        .x-button {
          z-index: 1;
          transition: all 0.3s;
          &:hover {
            transition: all 0.3s;
            color: red;
          }
        }
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
