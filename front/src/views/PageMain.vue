<template>
  <div>
    <div class="block-1" data-aos="fade-up">
      <div class="info" data-aos="fade-up" data-aos-delay="500">
        <img src="../assets/img/logo_black.png" alt="" />
      </div>
    </div>
    <div class="block-2" data-aos="fade-down" data-aos-duration="1000">
      <div class="info">
        <div class="flex-item">
          <ul>
            <li data-aos="fade-right" data-aos-delay="800">
              <span class="initial">M</span><span>y</span><br />
            </li>
            <li data-aos="fade-right" data-aos-delay="900">
              <span class="initial">A</span><span>sset</span><br />
            </li>
            <li data-aos="fade-right" data-aos-delay="1000">
              <span class="initial">M</span><span>anager</span><br />
            </li>
          </ul>
          <button
            data-aos="fade-right"
            data-aos-delay="1100"
            class="btn btn-outline-dark flex-item"
            @click="this.$router.push('/board')"
          >
            이동
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
    };
  },
  methods: {},
};
</script>
<style lang="scss" scoped>
body {
  margin: 0;
}

img {
  width: 40vw;
}

ul {
  list-style-type: none;
}

@mixin block {
  width: 100vw;
  height: 100vh;
  display: flex;
  font-size: 20vw;
  align-items: center;
}
.info {
  flex: 1;
  align-items: center;
}

.block-1 {
  @include block;
  background-image: url("../assets/img/main-bg.jpg");
  background-size: cover; /* 요소를 비율에 맞게 커버 */
  background-attachment: fixed;
}

.block-2 {
  @include block;
  background-color: rgb(93, 92, 94);
  font-size: 5vw;
  color: white;
  letter-spacing: 1vw;
}

@media (min-width: 768px) {
  .btn {
    width: 10vw;
    height: 8vh;
  }
}

@media (max-width: 767px) {
  .btn {
    width: 10vw;
    height: 8vh;
  }
}
</style>
