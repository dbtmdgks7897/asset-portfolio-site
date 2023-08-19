<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <span>포트폴리오 관리</span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <div class="flex title">
          <p class="table-title">보유 자산</p>
          <a href="#bookmarkList">즐겨찾기 바로가기</a>
        </div>
        <a name="#assetList"></a>
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">코드</th>
              <th>이름</th>
              <th>총액</th>
              <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr
              style="font-weight: bold"
              v-for="detail in listData"
              :key="detail"
            >
              <th scope="row">{{ detail.code }}</th>
              <td>{{ detail.name }}</td>
              <td>{{ (detail.amount * detail.purchasePrice).toLocaleString() }} 원</td>
              <td>
                <button
                  class="btn my-button"
                  @click="assetDeleteButton(detail.code)"
                >
                  <span><i class="bi bi-x"></i></span>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <br />
        <div class="flex title">
          <p class="table-title">즐겨찾기</p>
          <a href="#assetList">보유 자산 바로가기</a>
        </div>
        <a name="#bookmarkList"></a>
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">코드</th>
              <th>이름</th>
              <th>현재가</th>
              <th>카테고리(클릭 시 이동)</th>
              <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr v-for="bookmark in bookmarkListData" :key="bookmark">
              <th scope="row">{{ bookmark.code }}</th>
              <td>{{ bookmark.name }}</td>
              <td>{{ bookmark.price != null ? bookmark.price.toLocaleString() : null }}원</td>
              <td>
                <a
                  class="category-link"
                  @click="categoryButtonHandler(bookmark.type)"
                >
                  {{ bookmark.type }}</a
                >
              </td>
              <td>
                <button class="btn my-button" @click="deleteBookmarkButton(bookmark.code)">
                  <span><i class="bi bi-x"></i></span>
                </button>
              </td>
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
      diviUpdating: false,
      listData: null,
      bookmarkListData: null,
      portfolioIdx: localStorage.getItem("portfolioIdx"),
    };
  },
  mounted() {
    this.getPortfolioDetailList();
    this.getBookmarkList();
  },
  methods: {
    getPortfolioDetailList() {
      this.$axios
        .get(`/api/v1/portfolio/detail/${this.portfolioIdx}/list`)
        .then((res) => {
          if (res.data.code === 0) {
            this.listData = res.data.data.detailList;
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
    assetDeleteButton(code) {
      const check = prompt(
        `정말 삭제하시겠습니까? \n삭제하시려면 코드를 정확하게 입력해주세요.\n${code}`
      );
      if (check === code) {
        const dto = {
          code: code,
        };
        this.$axios
          .post(`/api/v1/portfolio/detail/${this.portfolioIdx}`, dto, {
            headers: {
              "Content-Type": "application/json;charset=utf-8",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              alert(res.data.message);
              this.getPortfolioDetailList;
            } else {
              if (res.data.data != null) {
                alert(res.data.data);
              }
            }
          })
          .catch((err) => {
            alert(err.response.data.message);
          });
      }
    },
    getBookmarkList() {
      this.$axios
        .get("/api/v1/bookmark")
        .then((res) => {
          if (res.data.code === 0) {
            this.bookmarkListData = res.data.data.bookmarkList;
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
    categoryButtonHandler(category) {
      switch (category) {
        case "주식_국내":
          this.$router.push("/asset/stock/domestic");
          break;
        case "주식_해외":
          this.$router.push("/asset/stock/overseas");
          break;
        case "암호화폐":
          this.$router.push("/asset/bitcoin");
          break;
        case "외화":
          this.$router.push("/asset/currency");
          break;
        case "기타":
          this.$router.push("/asset/custom");
          break;
        default:
          alert("어떤 값인지 파악이 되지 않습니다.");
      }
    },
    deleteBookmarkButton(code) {
      if (confirm("즐겨찾기를 삭제하시겠습니까?")) {
        const dto = {
          assetIdx: code,
        };
        this.$axios
          .post(`/api/v1/bookmark`, dto, {
            headers: {
              "Content-Type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              this.getBookmarkList();
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
  margin: 0;
  width: 100%;
  height: 100%;
  min-height: 100vh;
  &-head {
    display: flex;
    height: 10%;
    margin: 0;
    justify-content: normal;
    span {
      font-size: 4vw;
    }
  }
  &-body {
    .title {
      justify-content: space-between;
      align-items: center;

      .table-title {
        font-size: 5vh;
      }
    }


    .my-button {
      width: 3vw;
      height: 3vw;
      padding: 0;
      span {
        font-size: 2vh;
      }
    }

    .diviInput {
      width: 7vw;
    }

  }
}
</style>
