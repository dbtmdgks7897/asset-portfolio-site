<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>(post.title)</span>
        <div>
          <!-- 현재 사용자와 작성자의 id가 같을 시 보이게-->
          <!-- v-if -->
          <button class="btn btn-outline-dark" @click="updateButton">수정</button>
          <button class="btn btn-outline-dark" @click="deleteButton">
            삭제
          </button>
        </div>
      </div>
      <div class="contents-body">
        <div class="post-info flex">
          <span>(post.created_at)</span>
          <span>(post.user_idx)</span>
          <span>(post.recommend_count)</span>
        </div>
        <div class="post-content">(post.content)</div>
        <div class="post-buttons">
          <!-- 게시글 추천, 신고 -->
          <button class="btn btn-outline-dark" @click="reportButton">신고</button>
          <button class="btn btn-outline-dark" @click="recommendButton">
            추천 (post.recommend_count)
          </button>
        </div>
      </div>
      <div class="comment">
        <div class="comment-title">댓글</div>
        <div class="comment-main">
          <ul>
            <!-- v-for로 반복 돌림 -->
            <li class="flex comment-li">
              <div class="text">
                <span>(comment.user_idx)</span> |
                <span>(comment.created_at)</span><br />
                <span>(comment.content)</span>
              </div>

              <div class="buttons">
                <!-- comment 테이블에서 1대N 매핑한 후 -->
                <!-- 리스트 가져오기 -->
                <span>(추천 수)</span><br />
                <!-- 댓글 추천, 신고 -->
                <button class="btn btn-outline-dark" @click="comReportButton">
                  <i class="bi bi-cone"></i>
                </button>
                <button class="btn btn-outline-dark" @click="comRecommendButton">
                  <i class="bi bi-hand-thumbs-up-fill"></i>
                </button>
              </div>
            </li>
            <li class="flex comment-li">
              <div class="text">
                <span>(comment.user_idx)</span> |
                <span>(comment.created_at)</span><br />
                <span>(comment.content)</span>
              </div>

              <div class="buttons">
                <!-- comment 테이블에서 1대N 매핑한 후 -->
                <!-- 리스트 가져오기 -->
                <span>(추천 수)</span><br />
                <button class="btn btn-outline-dark">
                  <i class="bi bi-cone"></i>
                </button>
                <button class="btn btn-outline-dark">
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
</script>
<script>
export default {
  name: "PageBoardDetail",
  data() {
    //변수생성
    return {
      deleteAnswer: false,
      id: this.$route.params.id,
    };
  },
  methods: {
    updateButton() {
      this.$router.push({ name : 'PageBoardUpdate', params : {
        id: this.id
      }})
    },
    deleteButton() {
      this.deleteAnswer = confirm("삭제하시겠습니까?");
    },
    reportButton() {
      // 신고 내역 확인 후
      // 있으면 이미 신고한 게시물, 없으면 신고 내역 받아서 넘겨주기
      // prompt('신고 사유를 입력해주세용');
      alert('');
    },
    recommendButton() {
      // 추천 내역 확인 후
      // 있으면 추천 테이블에 추가 후 count 하나 올리기
      // 없으면 이미 추천한 게시물입니다
      // alert
      alert('');
    },
    comReportButton() {
      // 신고 내역 확인 후
      // 있으면 이미 신고한 게시물, 없으면 신고 내역 받아서 넘겨주기
      // prompt('신고 사유를 입력해주세용');
      alert('');
    },
    comRecommendButton() {
      // 추천 내역 확인 후
      // 있으면 추천 테이블에 추가 후 count 하나 올리기
      // 없으면 이미 추천한 게시물입니다
      // alert
      alert('');
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

@media (max-width: 767px) {
  .contents {
    width: 100%;
  }
}
</style>
