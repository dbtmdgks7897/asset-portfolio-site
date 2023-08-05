<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div class="contents-box">
        <div class="contents-head flex">
          <div class="flex">
            <img class="flex-item" :src="profile.img" alt="프로필 이미지" />
            <transition name="fade" mode="out-in">
              <input v-show="isUpdateInter" type="file" name="profile" />
            </transition>
          </div>
          <div class="text">
            <input
              id="nicknameInput"
              type="text"
              value="(user.nickname)"
              disabled
              readonly
            />
            <span id="name">(user.name)</span>
          </div>
        </div>
        <div class="contents-body">
          <ul>
            <li class="flex">
              <span>이메일</span>
              <input type="text" value="(user.email)" disabled readonly />
            </li>
            <li class="flex">
              <span>성별</span>
              <select
                id="genderSelect"
                class="form-select form-select-sm"
                aria-label="Small select example"
                disabled
              >
                <option selected>성별</option>
                <option value="1">남성</option>
                <option value="2">여성</option>
              </select>
            </li>
            <li class="flex">
              <span>나이</span>
              <select
                id="ageSelect"
                class="form-select form-select-sm"
                aria-label="Small select example"
                disabled
              >
                <option selected>나이</option>
                <option v-for="val in 100" :key="val" :value="val">
                  {{ val }}
                </option>
              </select>
            </li>
            <li class="buttons">
              <transition name="fade" mode="out-in">
                <button
                  v-if="!isUpdateInter"
                  class="btn my-button"
                  @click="changeUpdateInter"
                >
                  <span>수정</span>
                </button>
              </transition>
            </li>
            <li class="buttons">
              <transition name="fade" mode="out-in">
                <button
                  v-if="!isUpdateInter"
                  class="btn red-button"
                  @click="leave"
                >
                  <span>회원 탈퇴</span>
                </button>
                <button v-else class="btn my-button" @click="changeUpdateInter">
                  <span>수정 완료</span>
                </button>
              </transition>
            </li>
          </ul>
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
      isUpdateInter: false,

      profile: {
        name: "anonymous",
        img: require("../../assets/img/anonymous.png"),
      },
    };
  },
  methods: {
    changeUpdateInter() {
      document.querySelector("#nicknameInput").disabled = this.isUpdateInter;
      document.querySelector("#genderSelect").disabled = this.isUpdateInter;
      document.querySelector("#ageSelect").disabled = this.isUpdateInter;
      this.isUpdateInter = !this.isUpdateInter;
    },
    update() {
      confirm("");
    },
    leave() {
      prompt("");
    },
  },
};
</script>
<style lang="scss" scoped>
.contents {
  margin: 0;
  width: 100%;
  &-box {
    width: 40vw;
    height: 40vh;
    margin: 20vh auto;
  }
  &-head {
    height: 40%;
    font-size: 2vw;
    // line-height: 1vh;
    justify-content: normal;

    .flex {
      width: 30%;
      flex-direction: column;
      font-size: 1;
      input {
        font-size: small;
      }
    }

    img {
      width: 5vw;
    }
    .text {
      text-align: left;
      margin-left: 2vw;
      #name {
        font-weight: normal;
      }
    }
  }
  &-body {
    height: 60%;
    li {
      margin: 3vh 0;
      input {
        width: 60%;
      }
      select {
        width: 30%;
      }
      span {
        width: 5vw;
      }
      .my-button {
        height: 4vh;
      }
      .red-button {
        $main-color: red;
        $sub-color: white;

        width: 6vw;
        height: 4vh;
        border-color: $main-color;
        &:hover {
          background-color: $main-color;
          border-color: $sub-color;
          color: $sub-color;
          span {
            color: $sub-color;
          }
        }
        span {
          text-align: center;
          font-size: 1vw;
          color: $main-color;
        }
      }
    }
    .buttons {
      margin: 2% 5%;
      text-align: right;
    }
  }
}
</style>
