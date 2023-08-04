<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>게시물 수정</span>
      </div>
      <div class="contents-body">
        <div class="mb-3">
          <input
            type="email"
            class="form-control"
            id="exampleFormControlInput1"
            value="(post.title)"
          />
        </div>
        <div class="mb-3">
          <textarea
            class="form-control"
            id="exampleFormControlTextarea1"
            rows="20"
          >(post.content)</textarea>
        </div>
      </div>
      <div class="buttons">
        <!-- 취소 클릭 시 해당 게시물 상세 페이지로 -->
        <button
          class="btn btn-outline-dark cancle-button"
          @click="cancelButton"
        >
          <span>취소</span>
        </button>
        <!-- 완료 클릭 시 -->
        <!-- 데이터 유효성 검사 후 값을 받아 -->
        <!-- 데이터를 post에 저장 -->
        <button class="btn btn-outline-dark posting-button" @click="submitButton">
          <span>완료</span>
        </button>
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
  name : 'PageBoardUpdate',
  data() {
    return {
      width: window.innerWidth || document.body.clientWidth,
      height: window.innerHeight || document.body.clientHeight,
      
    };
  },
  methods :{
    cancelButton() {
      confirm('')
      this.$router.push({name : 'PageBoardDetail', params : {
        id: this.$route.params.id,
      }})
      
    },
    submitButton() {
      confirm('')
      this.$router.push({name : 'PageBoardDetail', params : {
        id: this.$route.params.id,
      }})
    },
  }
};
</script>
<style lang="scss" scoped>
.contents {
  &-head {
    span {
      font-size: 3vw;
    }
  }
  &-body {
    width: 100%;
    div {
      text-align: left;
      input {
        font-size: 2vw;
      }
    }
  }
}

.posting-button {
  width: 6vw;
  height: 5vw;
  margin: 2vw;
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

.cancle-button {
  width: 6vw;
  height: 5vw;
  border-color: red;
  &:hover {
    background-color: red;
    border-color: white;
    color: white;
    span {
      color: white;
    }
  }
  span {
    text-align: center;
    font-size: 1vw;
    color: red;
  }
}

@media (max-width: 767px) {
  .contents {
    width: 100%;
  }
}
</style>
