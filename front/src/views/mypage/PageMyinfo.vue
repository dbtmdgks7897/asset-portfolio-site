<template>
  <div :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']">
    <div class="contents">
      <div v-if="myData" class="contents-box">
        <div class="contents-head flex">
          <div class="flex">
            <img
              class="flex-item"
              :src="myData.profileImg"
              alt="프로필 이미지"
            />
            <transition name="fade" mode="out-in">
              <input
                v-if="isUpdateInfo"
                id="file-input"
                type="file"
                name="profile"
                accept="image/*"
                @change="onFileSelected"
              />
            </transition>
          </div>
          <div class="text">
            <input
              v-model="myData.nickname"
              id="nicknameInput"
              class="changeableInput"
              style="display: block"
              type="text"
              disabled
            />
            <span v-if="!myData.nickname" class="error-span"
              >닉네임을 입력해주세요</span
            ><br />
            <span id="name">{{ myData.name }}</span>
          </div>
        </div>
        <div class="contents-body">
          <ul>
            <li class="flex">
              <span>이메일</span>
              <input v-model="myData.email" type="text" disabled readonly />
            </li>
            <li class="flex">
              <span>전화번호</span>
              <input
                v-model="myData.phone"
                class="changeableInput"
                type="text"
                disabled
              /><br />
            </li>
            <span
              v-if="
                !(myData.phone.length == 13) ||
                !(myData.phone.split('-')[0] == '010')
              "
              class="error-span"
              >전화번호를 제대로 입력해주세요</span
            >
            <li class="flex">
              <span>성별</span>
              <select
                v-model="myData.gender"
                id="genderSelect"
                class="form-select form-select-sm changeableInput"
                aria-label="Small select example"
                disabled
              >
                <option value="" selected>성별</option>
                <option value="남성">남성</option>
                <option value="여성">여성</option>
              </select>
            </li>
            <li class="flex">
              <span>나이</span>
              <select
                v-model="myData.age"
                id="ageSelect"
                class="form-select form-select-sm changeableInput"
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
                  v-if="!isUpdateInfo"
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
                  v-if="!isUpdateInfo"
                  class="btn red-button"
                  @click="leaveButton"
                >
                  <span>회원 탈퇴</span>
                </button>
                <button v-else class="btn my-button" @click="updateButton">
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
import { login } from "@/utils/login";
// import { imgTrans } from "@/utils/imgTrans";
</script>
<script>
export default {
  data() {
    return {
      selectedFile: null,
      isUpdateInfo: false,
      myData: null,
      anonProfileImg: require("../../assets/img/anonymous.png"),
    };
  },
  watch() {
    // 총 길이가 13, 앞에서 3글자가 010
  },
  mounted() {
    this.getMyinfoInitData();
  },
  methods: {
    onFileSelected(event) {
      this.selectedFile = event.target.files[0];
    },
    changeUpdateInter() {
      document.querySelectorAll(".changeableInput").forEach((element) => {
        element.disabled = this.isUpdateInfo;
      });
      this.isUpdateInfo = !this.isUpdateInfo;
    },
    updateButton() {
      // const dto = {
      //   idx: login.idx,
      //   profileImg: this.myData.profileImg.split(',')[1],
      //   imgType: this.myData.imgType,
      //   nickname: this.myData.nickname,
      //   gender: this.myData.gender,
      //   age: this.myData.age,
      //   phone: this.myData.phone
      // }
      // const dto = {
      //   nickname: this.myData.nickname,
      //   gender: this.myData.gender,
      //   age: this.myData.age,
      //   phone: this.myData.phone,
      // };

      // console.log(dto.nickname)
      const formData = new FormData();
      this.myData.age == null ? 0 : this.myData.age;
      formData.append("file", this.selectedFile);
      // formData.append("dto", dto);
      // formData.append("profileImg", this.myData.profileImg);
      // formData.append("imgType", this.myData.imgType);
      formData.append("nickname", this.myData.nickname);
      formData.append("gender", this.myData.gender);
      formData.append("age", this.myData.age);
      formData.append("phone", this.myData.phone);

      // console.log(formData.get("idx"));

      // myData의 name만 지금 이름으로
      this.$axios
        .post(`/api/v1/mypage/info/update`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data);
            this.changeUpdateInter();
            this.getMyinfoInitData();
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          // alert(err.response.data.message);
          console.log(err);
        });
    },
    getMyinfoInitData() {
      this.$axios
        .get(`/api/v1/mypage/info`)
        .then((res) => {
          if (res.data.code === 0) {
            this.myData = res.data.data;
            // this.myData.profileImg = ;
            console.log(this.myData.profileImg);
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    leaveButton() {
      if (
        prompt(
          `회원을 탈퇴하시려면 아래의 닉네임을 정확히 입력하여 주세요. \n${this.myData.nickname}`
        ) === this.myData.nickname
      ) {
        this.$axios
          .delete(`/api/v1/mypage/info`)
          .then((res) => {
            if (res.data.code === 0) {
              // TODO : 세션 삭제 후 메인페이지로
              this.$axios.get(`/api/v1/auth/logout`).then(() => {
                alert("회원 탈퇴 되었습니다. \n 게시판 페이지로 이동합니다");
                login.isLogined = false;
                login.email = null;
                this.$router.push({ name: "PageBoardList" });
              });
              this.myData = res.data.data;
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
  .error-span {
    color: red;
  }
}
</style>
