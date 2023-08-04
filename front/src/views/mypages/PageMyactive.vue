<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head">
        <img class="flex-item" :src="profile.img" alt="프로필 이미지" />
        <span>(user.name)</span>
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
                <tr>
                  <th scope="row">---------------(post.title)------------</th>
                  <td>(post.created_at)</td>
                  <td>1</td>
                  <td>1</td>
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
                  <th scope="col">제목</th>
                  <th scope="col">날짜</th>
                  <th scope="col">추천</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th scope="row">-------------(comment.title)----------</th>
                  <td>(comment.created_at)</td>
                  <td>1</td>
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
</script>
<script>
export default {
  data() {
    return {
      profile: {
        name: "anonymous",
        img: require("../../assets/img/anonymous.png"),
      },
    };
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
