<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <img class="flex-item" :src="login.img" alt="프로필 이미지" />
        <span>{{ login.nickname }}</span>
      </div>
      <div class="contents-body">
        <div class="contents-body-boards">
          <div class="body-head">
            <span class="title">내가 쓴 글</span>
            <span>날짜 | 조회수 | 추천수</span>
          </div>
          <div class="body-body">
            <table class="table">
              <thead class="table-light">
                <tr>
                  <th scope="col">제목</th>
                  <th scope="col">날짜</th>
                  <th scope="col">조회수</th>
                  <th scope="col">추천</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="board in boardList" :key="board">
                  
                    <th scope="row"><a @click="this.$router.push(`/board/${board.idx}`)">{{ util.truncateText(board.name, 15) }}</a></th>
                    <td>{{ board.createdAt }}</td>
                    <td>{{ board.viewCount }}</td>
                    <td>{{ board.recommendCount }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="contents-body-comments">
          <div class="body-head">
            <span class="title">내가 쓴 댓글</span>
            <span>날짜 | 추천수</span>
          </div>
          <div class="body-body">
            <table class="table">
              <thead class="table-light">
                <tr>
                  <th scope="col">게시물 제목</th>
                  <th scope="col">내용</th>
                  <th scope="col">날짜</th>
                  <th scope="col">추천</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="comment in commentList" :key="comment">
                  <th scope="row"><a @click="this.$router.push(`/board/${comment.board.idx}`)">{{ util.truncateText(comment.board.name, 15) }}</a></th>
                  <td>{{ comment.content }}</td>
                  <td>{{ comment.createdAt }}</td>
                  <td>{{ comment.recommendCount }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { util } from "@/utils/utils";
import { login } from "@/utils/login";

</script>
<script>
export default {
  data() {
    return {
      boardList: null,
      commentList: null,
      profile: {
        name: "anonymous",
        img: require("../../assets/img/anonymous.png"),
      },
    };
  },
  mounted() {
    this.getMyactiveData();
    login.getUserProfile();
  },
  methods: {
    getMyactiveData() {
      this.$axios
        .get(`/api/v1/mypage/active`)
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.data.boardList);
            this.boardList = res.data.data.boardList;
            this.commentList = res.data.data.commentList;
          } else {
            alert(res.data.message);
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
.contents {
  margin: 0;
  width: 100%;
  &-head {
    display: flex;
    height: 10%;
    padding: 0 5%;
    justify-content: normal;
    img {
      margin-right: 5vw;
      width: 3vw;
    }
    span {
      font-size: 2vw;
    }
  }

  &-body {
    display: flex;
    justify-content: space-around;

    div {
      width: 35vw;
      height: 60vh;
      .body-head {
        display: flex;
        padding: 1vw;
        justify-content: space-between;
        align-items: center;
        border-bottom: 2px solid gray;
        width: 100%;
        height: 15%;
        .title {
          font-size: 2.5vw;
        }
        span {
          font-size: 1vw;
        }
      }
    }
  }
}
</style>
