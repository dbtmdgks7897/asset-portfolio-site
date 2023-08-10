<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-head flex">
        <span>게시물 작성</span>
      </div>
      <div class="contents-body">
        <div class="mb-3">
          <input
            type="email"
            v-model="dto.name"
            class="form-control"
            id="exampleFormControlInput1"
            placeholder="제목을 입력해주세요"
          />
        </div>
        <div class="mb-3">
          <textarea
            class="form-control"
            v-model="dto.content"
            id="exampleFormControlTextarea1"
            placeholder="내용을 입력해주세요"
            rows="20"
          ></textarea>
        </div>
      </div>
      <div class="buttons">
        <!-- 취소 클릭 시 List페이지로 -->
        <button
          class="btn btn-outline-dark cancel-button"
          @click="cancelButton"
        >
          <span>취소</span>
        </button>
        <!-- 완료 클릭 시 -->
        <!-- 데이터 유효성 검사 후 값을 받아 -->
        <!-- 데이터를 post에 저장 -->
        <button
          class="btn btn-outline-dark posting-button"
          @click="submitButton"
        >
          <span>완료</span>
        </button>
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
export default {
  data() {
    return {
      width: window.innerWidth || document.body.clientWidth,
      height: window.innerHeight || document.body.clientHeight,
      dto: {
        userIdx: login.idx,
        name: null,
        content: null,
      },
    };
  },
  methods: {
    cancelButton() {
      if (confirm("취소하시겠습니까?")) {
        this.$router.push({ name: "PageBoardList" });
      }
    },
    submitButton() {
      if (confirm("게시하시겠습니까?")) {
        this.$axios
          .post("/api/v1/board", this.dto, {
            headers: {
              "Content-Type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              console.log(res.data.data);
              alert("게시물이 저장되었습니다.");
              this.$router.push({
                name: "PageBoardDetail",
                params: {
                  id: res.data.data,
                },
              });
            } else {
              alert(res.data.message);
            }
          })
          .catch((err) => {
            alert(err);
          });
      }
    },
  },
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

.cancel-button {
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
