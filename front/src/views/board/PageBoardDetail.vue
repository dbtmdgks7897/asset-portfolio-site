<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="move-buttons">
      <button
        v-if="boardIdx != 1"
        class="btn detail-move-button1"
        @click="preDetail"
      >
        <span>
          <i class="bi bi-caret-left-fill"></i>
        </span>
      </button>
      <button class="btn detail-move-button2" @click="nextDetail">
        <span>
          <i class="bi bi-caret-right-fill"></i>
        </span>
      </button>
    </div>
    <div v-if="boardDetailsData" class="contents">
      <div class="move-div" style="display: flex">
        <button @click="this.$router.go(-1)" class="btn">
          <span><i class="bi bi-caret-left-fill"></i>뒤로가기</span>
        </button>
      </div>
      <div class="contents-head flex">
        <span class="auto-resize-text">{{ boardDetailsData.name }}</span>
        <div>
          <!-- 현재 사용자와 작성자의 id가 같을 시 보이게-->
          <!-- v-if -->
          <button
            v-if="login.isLogined && boardDetailsData.user.idx == login.idx"
            class="btn btn-outline-dark"
            @click="updateButton"
          >
            수정
          </button>
          <button
            v-if="login.isLogined && boardDetailsData.user.idx == login.idx"
            class="btn btn-outline-dark"
            @click="deleteButton"
          >
            삭제
          </button>
        </div>
      </div>
      <div class="contents-body">
        <div class="post-info flex">
          <span>작성일 :{{ boardDetailsData.createdAt }}</span>
          <span>작성자 : {{ boardDetailsData.user.nickname }}</span>
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
        <div class="flex comment-header">
          <div class="comment-title">댓글</div>
          <button
            v-if="login.isLogined"
            @click="commentShow = !commentShow"
            class="btn comment-button"
          >
            <span>댓글창</span>
          </button>
        </div>
        <div class="comment-main">
          <div v-show="commentShow" class="comment-write flex">
            <span>내용</span>
            <div class="input-group mb-3">
              <input
                v-model="commentContent"
                type="text"
                class="form-control"
                placeholder=""
                aria-describedby="button-addon2"
                @keydown.enter="commentButton"
              />
              <button
                class="btn btn-outline-secondary"
                type="button"
                id="button-addon2"
                @click="commentButton"
              >
                Button
              </button>
            </div>
          </div>
          <ul v-if="boardCommentListData">
            <!-- v-for로 반복 돌림 -->
            <li
              v-for="comment in boardCommentListData"
              :key="comment"
              class="flex-item flex comment-li"
            >
              <div class="text">
                <span>{{ comment.user.nickname }}</span> |
                <span>{{ comment.createdAt }}</span
                ><br />
                <input
                  v-if="commentRewriteBoxShow && login.idx == comment.user.idx"
                  type="text"
                  id="commentRewriteContent"
                  :value="comment.content"
                  @keydown.enter="
                    comRewriteButton(comment.idx);
                    commentRewriteBoxShow = false;
                  "
                />
                <span class="comment-content" v-else>{{
                  comment.content
                }}</span>
              </div>
              <div
                v-if="login.isLogined && login.idx == comment.user.idx"
                class="comment-tool"
              >
                <button
                  @click="commentRewriteBoxShow = !commentRewriteBoxShow"
                  class="btn comment-tool-button"
                >
                  <span><i class="bi bi-pencil-square"></i></span>
                </button>
                <button
                  @click="comDeleteButton(comment.idx)"
                  class="btn comment-tool-button"
                >
                  <span><i class="bi bi-x-lg"></i></span>
                </button>
              </div>
              <div v-if="login.isLogined" class="buttons">
                <!-- comment 테이블에서 1대N 매핑한 후 -->
                <!-- 리스트 가져오기 -->
                <span>{{ comment.recommendCount }}</span
                ><br />
                <!-- 댓글 추천, 신고 -->
                <button
                  class="btn btn-outline-dark"
                  @click="comReportButton(comment.idx)"
                >
                  <i class="bi bi-cone"></i>
                </button>
                <button
                  class="btn btn-outline-dark"
                  @click="comRecommendButton(comment.idx)"
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
import { login } from "@/utils/login";
import { toggle } from "@/utils/toggle";
</script>
<script>
import router from "@/router";
export default {
  name: "PageBoardDetail",
  data() {
    //변수생성
    return {
      deleteConfirm: false,
      boardIdx: this.$route.params.id,
      boardDetailsData: null,
      boardCommentListData: null,
      router,

      commentContent: null,
      commentShow: false,
      commentRewriteContent: null,
      commentRewriteBoxShow: false,
    };
  },
  mounted() {
    this.getBoardDetailsFn();
    this.getBoardCommentListFn();
  },
  methods: {
    nextDetail() {
      const next = parseInt(this.boardIdx) + 1;
      this.$router.push({
        name: "PageBoardDetail",
        params: { id: next },
      });
      this.boardIdx = next;
      this.getBoardDetailsFn()
      this.getBoardCommentListFn()
    },
    preDetail() {
      const pre = parseInt(this.boardIdx) - 1;
      this.$router.push({
        name: "PageBoardDetail",
        params: { id: pre },
      });
      this.boardIdx = pre;
      this.getBoardDetailsFn()
      this.getBoardCommentListFn()
    },
    getBoardDetailsFn() {
      this.$axios
        .get(`/api/v1/board/` + this.boardIdx)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.boardDetailsData = res.data.data;
          }
        })
        .catch((err) => {
          alert(err.response.data.message)
          this.$router.go(-1)
        });
    },
    getBoardCommentListFn() {
      this.$axios
        .get(`/api/v1/board/${this.boardIdx}/comment`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data);
            this.boardCommentListData = res.data.data.commentList;
          } else if (res.data === "") {
            this.boardCommentListData = null;
          }
        })
        .catch((res) => {
          console.log(res);
        });
    },
    updateButton() {
      if (login.idx === this.boardDetailsData.user.idx) {
        this.$router.push({
          name: "PageBoardUpdate",
          params: {
            id: this.boardIdx,
          },
        });
      } else {
        alert("돌아가");
      }
    },
    deleteButton() {
      this.deleteConfirm = confirm("삭제하시겠습니까?");
      if (this.deleteConfirm) {
        if (login.idx === this.boardDetailsData.user.idx) {
          this.$axios
            .delete(`/api/v1/board/${this.boardIdx}`)
            .then((res) => {
              if (res.data.code === 0) {
                alert("게시물이 삭제되었습니다.");
                this.$router.push({ name: "PageBoardList" });
              } else {
                console.log(res);
              }
            })
            .catch((err) => {
              alert(err.response.data.message);
            });
        } else {
          console.log("누구냐 너");
        }
      }
    },
    reportButton() {
      const reportReason = prompt("신고 사유를 입력해주세요");
      if (reportReason != null) {
        const dto = {
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
              this.getBoardDetailsFn();
            }
          })
          .catch((err) => {
            alert(err.response.data.message);
          });
      }
    },
    recommendButton() {
      this.$axios
        .post(`/api/v1/board/${this.boardIdx}/recommend`, {
          headers: {
            "content-type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert("추천 성공");
            this.getBoardDetailsFn();
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    commentButton() {
      const dto = {
        content: this.commentContent,
      };
      this.$axios
        .post(`/api/v1/comment/${this.boardIdx}`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardCommentListFn();
            this.commentShow = false;
          } else {
            alert(res.data.data);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    comReportButton(commentIdx) {
      const reportReason = prompt("신고 사유를 입력해주세요");
      if (reportReason != null) {
        const dto = {
          reason: reportReason,
        };
        this.$axios
          .post(`/api/v1/comment/${commentIdx}/report`, dto, {
            headers: {
              "content-type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              alert("신고 성공");
              this.getBoardCommentListFn();
            } else {
              alert(res.data.message);
            }
          })
          .catch((err) => {
            alert(err.response.data.message);
          });
      }
    },
    comRecommendButton(commentIdx) {
      this.$axios
        .post(`/api/v1/comment/${commentIdx}/recommend`, {
          headers: {
            "content-type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert("추천 성공");
            this.getBoardCommentListFn();
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    comRewriteButton(commentIdx) {
      this.commentRewriteContent = document.querySelector(
        "#commentRewriteContent"
      ).value;
      const dto = {
        content: this.commentRewriteContent,
      };
      this.$axios
        .put(`/api/v1/comment/${commentIdx}`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardCommentListFn();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    comDeleteButton(commentIdx) {
      this.$axios
        .delete(`/api/v1/comment/${commentIdx}`)
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardCommentListFn();
          } else {
            alert(res.data);
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

.move-buttons {
  position: absolute;
  top: 40vh;
  span {
    font-size: 5vw;
  }
  button {
    width: 5vw;
    height: 30vh;
    padding: 0;
  }
  .detail-move-button1 {
    i {
      color: blueviolet;
    }
  }
  .detail-move-button2 {
    i {
      color: blueviolet;
    }
    margin-left: 70vw;
  }
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

    .comment-header {
      justify-content: space-between;
      border-bottom: 1px solid black;
      .comment-title {
        width: 100%;
        text-align: left;
        font-size: 2vw;
      }
      .comment-button {
        width: 7vw;
        height: 3vw;
        border-color: blueviolet;
        &:hover {
          background-color: blueviolet;
          border-color: rgb(61, 16, 102);
          color: white;
          span {
            color: white;
          }
        }
        span {
          text-align: center;
          font-size: 1vw;
          color: blueviolet;
        }
      }
    }

    .comment-main {
      .comment-write {
        height: 15vh;
        background-color: rgb(199, 199, 199);
        align-items: center;
        span {
          width: 5vw;
          font-size: 1.5vw;
        }
        div {
          height: 80%;
          margin: 2vh 2vw;
          button {
            height: 100%;
          }
        }
      }
      .comment-li {
        justify-content: space-between;
        padding: 5px 0px;
        border-bottom: 1px solid black;
        .text {
          max-width: 40vw;
          text-align: left;
        }

        .comment-content {
          font-weight: bold;
        }

        .comment-tool {
          margin-left: auto;
          .comment-tool-button {
            width: 2vw;
            height: 2vw;
            font-size: 0.5vw;
            border-color: blueviolet;
            padding: 0;
            margin: 1px;
            &:hover {
              background-color: blueviolet;
              border-color: rgb(61, 16, 102);
              color: white;
              span {
                color: white;
              }
            }
            span {
              text-align: center;
              font-size: 1vw;
              color: blueviolet;
            }
          }
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
