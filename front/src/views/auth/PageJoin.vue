<template>
  <div class="content flex">
    <div class="">
      <img src="../../assets/img/logo_black.png" alt="" />
    </div>
    <span id="title">회원가입</span><br />
    <div class="col-md-8">
      <input
        v-model="name"
        type="text"
        class="form-control"
        placeholder="이름 (*)"
        required
      />
      <div class="invalid-feedback">이름을 입력해주세요</div>
    </div>
    <div class="col-md-8">
      <input
        v-model="phone"
        type="text"
        class="form-control"
        placeholder="전화번호(구분은 '-')"
        required
      />
    </div>
    <div class="col-md-8">
      <div class="input-group has-validation">
        <span class="input-group-text" id="inputGroupPrepend">@</span>
        <input
          type="text"
          v-model="email"
          class="form-control"
          aria-describedby="inputGroupPrepend"
          placeholder="Email (*)"
          required
        />
        <div class="invalid-feedback">이메일을 입력해주세요</div>
      </div>
    </div>
    <div class="col-md-8">
      <div class="input-group has-validation">
        <span class="input-group-text" id="inputGroupPrepend"
          ><i class="bi bi-key-fill"></i
        ></span>
        <input
          type="password"
          v-model="password"
          class="form-control"
          aria-describedby="inputGroupPrepend"
          placeholder="Password (*)"
          required
        />
        <div class="invalid-feedback">비밀번호를 입력해주세요</div>
      </div>
    </div>
    <div class="col-md-8">
      <input
        v-model="nickname"
        type="text"
        class="form-control"
        placeholder="닉네임 (*)"
        required
      />
      <div class="invalid-feedback">닉네임을 입력해주세요</div>
    </div>
    <button class="btn my-button" @click="joinButton">
      <span>가입하기</span>
    </button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      name: null,
      phone: null,
      email: null,
      password: null,
      nickname: null,
    };
  },

  methods: {
    joinButton() {
      const params = {
        user: {
          name: this.name,
          phone: this.phone,
          email: this.email,
          password: this.password,
          nickname: this.nickname,
        },
      };
      console.log("---axios Post 시작----");
      this.$axios
        .post(`api/v1/auth/join`, params, {
          header: {
            "Content-type": "application/json;charset=utf-8",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert("회원가입 성공!");
            this.$router.push({ name: "PageLogin" });
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
.content {
  margin: 15vh auto;
  width: 500px;
  height: 80vh;
  border-radius: 1%;
  background: #fff;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  #title {
    font-weight: bold;
    font-size: 2vw;
  }

  img {
    width: 10vw;
  }

  .col-md-8 {
    margin: 2% 0;
    input {
      height: 5vh;
    }
  }

  .join-button {
    $main-color: blueviolet;
    $sub-color: white;

    width: 6vw;
    height: 5vh;
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
</style>
