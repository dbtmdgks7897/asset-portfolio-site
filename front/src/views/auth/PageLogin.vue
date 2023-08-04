<template>
  <div class="content flex">
    <div class="">
      <img src="../../assets/img/logo_black.png" alt="" />
    </div>
    <span id="title">로그인</span><br />
    <div class="col-md-8">
      <div class="input-group has-validation">
        <span class="input-group-text" id="inputGroupPrepend">@</span>
        <input
          type="text"
          class="form-control"
          id="validationCustomUsername"
          aria-describedby="inputGroupPrepend"
          placeholder="Email (*)"
          required
        />
      </div>
    </div>
    <div class="col-md-8">
      <div class="input-group has-validation">
        <span class="input-group-text" id="inputGroupPrepend"
          ><i class="bi bi-key-fill"></i
        ></span>
        <input
          type="text"
          class="form-control"
          id="validationCustomUsername"
          aria-describedby="inputGroupPrepend"
          placeholder="Password (*)"
          required
        />
        <div class="invalid-feedback">이메일 또는 비밀번호를 확인해주세요</div>
      </div>
    </div>
    <div class="flex">
      <button class="btn my-button" @click="this.$router.push({name : 'PageJoin'})"><span>회원가입</span></button>
      <button class="btn my-button"><span>로그인</span></button>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style lang="scss" scoped>
.content {
  margin: 15vh auto;
  width: 500px;
  height: 60vh;
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

  .my-button{
    margin: 0 2vw;
  }
}
</style>
