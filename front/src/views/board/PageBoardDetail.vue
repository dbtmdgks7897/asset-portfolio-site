<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div v-if="boardDetailsData" class="contents">
      <div class="contents-head flex">
        <span class="auto-resize-text">{{ boardDetailsData.name }}</span>
        <div>
          <!-- 현재 사용자와 작성자의 id가 같을 시 보이게-->
          <!-- v-if -->
          <button class="btn btn-outline-dark" @click="updateButton">
            수정
          </button>
          <button class="btn btn-outline-dark" @click="deleteButton">
            삭제
          </button>
        </div>
      </div>
      <div class="contents-body">
        <div class="post-info flex">
          <span>작성일 :{{ boardDetailsData.createdAt }}</span>
          <span>작성자 : {{ boardDetailsData.user.name }}</span>
          <span>조회수 : {{ boardDetailsData.viewCount }}</span>
        </div>
        <div class="post-content">{{ boardDetailsData.content }}</div>
        <div v-if="login.isLogined" class="post-buttons">
          <!-- 게시글 추천, 신고 -->
          <button class="btn btn-outline-dark" @click="reportButton">
            신고
          </button>
          <button class="btn btn-outline-dark" @click="recommendButton">
            추천 ({{ boardDetailsData.recommendCount }})
          </button>
        </div>
      </div>
      <div class="comment">
        <div class="comment-title">댓글</div>
        <div class="comment-main">
          <ul>
            <!-- v-for로 반복 돌림 -->
            <li
              v-for="comment in boardCommentListData"
              :key="comment"
              class="flex-item flex comment-li"
            >
              <div class="text">
                <span>{{ comment.user.name }}</span> |
                <span>{{ comment.createdAt }}</span
                ><br />
                <span>{{ comment.content }}</span>
              </div>
              <div v-if="login.isLogined" class="buttons">
                <!-- comment 테이블에서 1대N 매핑한 후 -->
                <!-- 리스트 가져오기 -->
                <span>{{ comment.recommendCount }}</span
                ><br />
                <!-- 댓글 추천, 신고 -->
                <button class="btn btn-outline-dark" @click="comReportButton">
                  <i class="bi bi-cone"></i>
                </button>
                <button
                  class="btn btn-outline-dark"
                  @click="comRecommendButton"
                >
                  <i class="bi bi-hand-thumbs-up-fill"></i>
                </button>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <!-- 로그인 시에만 보이게 -->
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { login } from "@/utils/login";
</script>
<script>
import router from '@/router';
export default {
  name: "PageBoardDetail",
  data() {
    //변수생성
    return {
      deleteConfirm: false,
      boardIdx: this.$route.params.id,
      boardDetailsData: null,
      boardCommentListData: null,
    };
  },
  mounted() {
    this.getBoardDetailsFn();
    this.getBoardCommentListFn();
  },
  methods: {
    getBoardDetailsFn() {
      this.$axios
        .get(`/api/v1/board/` + this.boardIdx)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.boardDetailsData = res.data.data;
          }
        })
        .catch((res) => {
          console.log(res);
        });
    },
    getBoardCommentListFn() {
      this.$axios
        .get(`/api/v1/board/${this.boardIdx}/comment`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.boardCommentListData = res.data.data.commentList;
          }
        })
        .catch((res) => {
          console.log(res);
        });
    },
    deleteBoardFn() {
      this.$axios
        .delete(`/api/v1/board/${this.boardIdx}`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res);
          }
        })
        .catch((res) => {
          console.error(res);
        });
    },
    updateButton() {
      this.$router.push({
        name: "PageBoardUpdate",
        params: {
          id: this.boardIdx,
        },
      });
    },
    deleteButton() {
      this.deleteConfirm = confirm("삭제하시겠습니까?");
      if (this.deleteConfirm) {
        if (login.idx === this.boardDetailsData.user) {
          this.deleteBoardFn();
        } else {
          console.log("누구냐 너");
        }
      }
    },
    reportButton() {
      const reportReason = prompt("신고 사유를 입력해주세요");
      if(reportReason != null) {
        const dto = {
        userIdx: login.idx,
        boardIdx: this.boardIdx,
        reason: reportReason,
      };
      this.$axios
        .post(`/api/v1/board/${this.boardIdx}/report`, dto, {
          headers: {
            "content-type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert("신고 성공");
            router.go(0);
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err);
        });
      }
    },
    recommendButton() {
      const dto = {
        userIdx: login.idx,
        boardIdx: this.boardIdx,
      };
      this.$axios
        .post(`/api/v1/board/${this.boardIdx}/recommend`, dto, {
          headers: {
            "content-type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert("추천 성공");
            router.go(0);
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err);
        });
    },
    comReportButton() {
      // 신고 내역 확인 후
      // 있으면 이미 신고한 게시물, 없으면 신고 내역 받아서 넘겨주기
      // prompt('신고 사유를 입력해주세용');
      alert("");
    },
    comRecommendButton() {
      // 추천 내역 확인 후
      // 있으면 추천 테이블에 추가 후 count 하나 올리기
      // 없으면 이미 추천한 게시물입니다
      // alert
      alert("");
    },
  },
};
</script>
<style lang="scss" scoped>
ul {
  padding: 0;
  list-style-type: none;
}

button {
  width: 8vw;
  height: 5vh;
  margin: 0 1vw;
  font-size: 1vw;
}
.contents {
  height: 100%;
  &-body {
    width: 100%;
    height: 50vh;
    font-size: 1vw;
    border: 1px solid black;
    .post-info {
      width: 100%;
      height: 7%;
      justify-content: space-between;
      border-bottom: 1px solid black;
      vertical-align: middle;
      align-items: center;
    }
    .post-content {
      width: 100%;
      height: 75%;
      padding: 5%;
    }
    .post-buttons {
      button:nth-child(1) {
        $main-color: red;
        $sub-color: white;

        color: $main-color;
        border-color: $main-color;
        &:hover {
          background-color: $main-color;
          color: $sub-color;
          border-color: $sub-color;
        }
      }
      button:nth-child(2) {
        $main-color: blueviolet;
        $sub-color: white;

        color: $main-color;
        border-color: $main-color;
        &:hover {
          background-color: $main-color;
          color: $sub-color;
          border-color: $sub-color;
        }
      }
    }
  }
  .comment {
    width: 100%;
    padding: 2%;
    .comment-title {
      text-align: left;
      font-size: 2vw;
      border-bottom: 1px solid black;
    }
    .comment-main {
      .comment-li {
        justify-content: space-between;
        padding: 5px 0px;
        border-bottom: 1px solid black;
        .text {
          max-width: 40vw;
          text-align: left;
        }

        .buttons {
          border-left: 1px solid black;
          button {
            width: 2vw;
            padding: 0;
            height: 3vh;
          }
        }
      }
    }
  }
}

.auto-resize-text {
  max-width: 55%;
  font-size: clamp(
    12px,
    4vw,
    24px
  ); /* 최소 크기, 화면 너비에 따라 자동 크기 조절, 최대 크기 */
}

@media (max-width: 767px) {
  .contents {
    width: 100%;
  }
}
</style>
