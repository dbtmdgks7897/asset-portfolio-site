<template>
  <!-- <button @click="show = !show" class="btn btn-light btn-toggle">토글</button>
  <Transition name="sidebar-toggle">
    <div v-if="show" class="sidebar"></div>
  </Transition> -->
  <div>
    <button
      class="btn btn-outline-dark btn-toggle"
      @click="toggle.show = !toggle.show"
      :class="{ toggleshow: !toggle.show }"
    >
      <span v-if="toggle.show"><i class="bi bi-arrow-left"></i></span>
      <span v-else><i class="bi bi-arrow-right"></i></span>
    </button>
    <div class="sidebar" :class="{ toggleshow: toggle.show }">
      <ul class="list-group list-group-flush">
        <li class="list-group-item list-group-item-dark no-margin border-2px">
          <!-- Vuex나 create 등으로 현재 상태 받아오기 -->
          <!-- 인증 정보 있을 시엔 내 정보-->
          <!-- 인증 정보 없을 시엔 로그인 페이지 이동-->
          <a @click="profileClick">
            <div class="profile flex account">
              <img
                v-if="login.isLogined && login.img != null"
                class="flex-item"
                style="border-radius: 100%"
                :src="login.img"
                alt="프로필 이미지"
              />
              <span v-if="login.isLogined" class="flex-item name">{{
                login.nickname
              }}</span>
              <span
                v-else-if="!login.isLogined && !isSidebarHide"
                class="flex-item name"
                @click="this.$router.push('/login')"
                >로그인 하소</span
              >
              <span v-else>
                <i class="bi bi-box-arrow-in-left"></i>
              </span>

              <div class="side-item account-dropdown" v-if="login.isLogined">
                <!-- 내 계정 카테고리 -->
                <!-- 로그인 전엔 안보임 -->
                <div class="SMN_effect-24" v-if="!isSidebarHide">
                  <li class="list-group-item list-group-item-dark category">
                    내 계정
                  </li>
                  <li
                    class="list-group-item list-group-item-dark"
                    @click="this.$router.push('/mypage/info')"
                  >
                    <a><span>내 정보</span></a>
                  </li>
                  <li
                    class="list-group-item list-group-item-dark"
                    @click="this.$router.push('/mypage/active')"
                  >
                    <a><span>내 활동</span></a>
                  </li>
                  <li
                    class="list-group-item list-group-item-dark"
                    @click="logout"
                  >
                    <a><span>로그아웃</span></a>
                  </li>
                </div>
                <div class="SMN_effect-24" v-else>
                  <li
                    class="list-group-item list-group-item-dark category"
                    @click="this.$router.push('/mypage/info')"
                  >
                    <a><span>내 계정</span></a>
                  </li>
                </div>
              </div>
            </div>
          </a>
        </li>
        <br />
        <!-- 나중엔 -->

        <!-- 포트폴리오 카테고리 -->
        <!-- 로그인 전엔 안보임 -->
        <div class="side-item" v-if="login.isLogined">
          <div class="SMN_effect-24" v-if="!isSidebarHide">
            <li class="list-group-item list-group-item-dark category">자산</li>
            <li
              class="list-group-item list-group-item-dark"
              @click="this.$router.push('/portfolio')"
            >
              <a><span>내 포트폴리오</span></a>
            </li>
            <li
              class="list-group-item list-group-item-dark"
              @click="this.$router.push('/portfolio/asset')"
            >
              <a><span>자산 검색</span></a>
            </li>
            <li class="list-group-item list-group-item-dark">
              <a><span>분석</span></a>
            </li>
            <li
              class="list-group-item list-group-item-dark"
              @click="this.$router.push('/portfolio/transaction')"
            >
              <a><span>거래내역</span></a>
            </li>
          </div>
          <div class="SMN_effect-24" v-else>
            <li
              class="list-group-item list-group-item-dark category"
              @click="this.$router.push('/portfolio')"
            >
              <a><span>자산</span></a>
            </li>
          </div>
        </div>
        <br />
        <!-- 커뮤니티 카테고리 -->
        <!-- 로그인 안해도 보임 -->
        <div class="side-item SMN_effect-24" v-if="!isSidebarHide">
          <li class="list-group-item list-group-item-dark category">
            커뮤니티
          </li>
          <li
            class="list-group-item list-group-item-dark"
            @click="this.$router.push('/board')"
          >
            <a><span>자유 게시판</span></a>
          </li>
        </div>
        <div class="side-item SMN_effect-24" v-else>
          <li
            class="list-group-item list-group-item-dark category"
            @click="this.$router.push('/board')"
          >
            <a><span>게시판</span></a>
          </li>
        </div>
        <br />
        <!--  -->
        <div
          class="side-item"
          v-if="
            login.isLogined &&
            login.roles != null &&
            login.roles.find((obj) => obj.name === 'ROLE_ADMIN') != null
          "
        >
          <!-- 관리 페이지 -->
          <!-- 관리자 권한만 볼 수 있음 -->
          <div v-if="!isSidebarHide">
            <li class="list-group-item list-group-item-dark category">
              관리 페이지
            </li>
            <li
              class="list-group-item list-group-item-dark"
              @click="this.$router.push('/admin/user')"
            >
              <a><span>유저 관리</span></a>
            </li>
            <li
              class="list-group-item list-group-item-dark"
              @click="this.$router.push('/admin/board')"
            >
              <a><span>게시물 관리</span></a>
            </li>
          </div>
          <div class="SMN_effect-24" v-else>
            <li
              class="list-group-item list-group-item-dark category"
              @click="this.$router.push('/admin/user')"
            >
              <a><span>관리</span></a>
            </li>
          </div>
        </div>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { login } from "@/utils/login";
</script>
<script>
export default {
  name: "PageHeader",
  data() {
    return {
      // toggle: this.$toggle,
      // isActive: false,
      width: 0,
      isSidebarHide: false,
      flag: false,
    };
  },

  methods: {
    profileClick() {
      // 현재 로그인 상태를 가정하여, 조건에 따라 라우터 이동
      // 로그인 상태라면 true, 로그인하지 않은 상태라면 false로 변경
      if (login.isLogined) {
        // this.isActive = !this.isActive; // 로그인 상태에서 클릭 시 내 정보 페이지로 이동
        // this.$router.push("/"); // 실제 라우터 이동이 필요한 경우 주석 해제
        this.$router.push({ name: "PageMyinfo" });
      } else {
        // this.$router.push('/login'); // 실제 라우터 이동이 필요한 경우 주석 해제
        this.$router.push({ name: "PageLogin" });
      }
    },
    logout() {
      if (confirm("로그아웃 하시겠습니까?")) {
        this.$axios.get(`/api/v1/auth/logout`).then(() => {
          alert("로그아웃 되었습니다. \n 게시판 페이지로 이동합니다");
          login.isLogined = false;
          login.email = null;
          this.$router.push({ name: "PageBoardList" });
        });
      }
    },
    handleResize() {
      this.width = window.innerWidth;
      if (this.width <= 760) {
        this.isSidebarHide = true;
      } else {
        this.isSidebarHide = false;
      }
    },
  },
  mounted() {
    login.getUserProfile();
    this.handleResize();
    window.addEventListener("resize", this.handleResize);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.handleResize);
  },
};
</script>
<style lang="scss" scoped>
$sidebar-width: 15vw;

body {
  font-size: 1vw;
}

ul {
  list-style-type: none;
}

.category {
  font-weight: bold;
  font-size: 2vw;
  text-align: left;
  margin-left: 2vw;
  color: rgb(153, 153, 153) !important;
}

.flex {
  display: flex;
  justify-content: space-between;
}
.flex-item {
  line-height: 3vw;
}

.profile {
  img {
    width: 3vw;
  }
  .name {
    font-size: 1.5vw;
  }
}

@media (max-width: 767px) {
  .profile {
    .name {
      display: none;
    }
  }
}

.side-item {
  li {
    font-size: 1.2vw;
    padding: 0;
  }
}

.list-group-item {
  background-color: rgba(255, 255, 255, 0);
  border: 0px;
  color: white;
  font-size: 1.5vh;
}

.border-2px {
  border-bottom: 2px solid black;
}

@media (min-width: 767px) {
  .sidebar {
    width: $sidebar-width;
    height: 100vh;
    background-color: blueviolet;
    top: 0;
    position: fixed;
    left: -$sidebar-width;
    transition: 1s;
    border: 2px solid black;
  }

  .btn-toggle {
    width: 5vw;
    height: 5vw;
    left: 7vw;
    top: 90vh;
    position: fixed;
    z-index: 1;
    transition: 1s;
    border-radius: 100%;
    opacity: 0.1;
    &:hover {
      opacity: 1;
    }
  }
}

@media (max-width: 767px) {
  .btn-toggle {
    left: $sidebar-width;
    top: 50vh;
    position: fixed;
    z-index: 1;
    transition: 1s;
    border-radius: 100%;
  }

  .sidebar {
    width: $sidebar-width;
    height: 100vh;
    background-color: blueviolet;
    top: 0;
    position: fixed;
    left: -$sidebar-width;
    transition: 1s;
    border: 2px solid black;
  }
}

.toggleshow {
  left: 0;
  transition: 1s;
}

.SMN_effect-24 a {
  padding: 10px 20px;
  position: relative;
  overflow: hidden;
  display: inline-block;
  -webkit-transition: 0.3s ease-out;
  -moz-transition: 0.3s ease-out;
  transition: 0.3s ease-out;
}

.SMN_effect-24 a span:before {
  width: 5px;
  height: 5px;
  background: transparent;
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  border-top: 2px solid #34645a;
  border-left: 2px solid #218c74;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  transition: 0.3s;
  opacity: 0;
}

.SMN_effect-24 a span:after {
  width: 6px;
  height: 6px;
  background: transparent;
  content: "";
  position: absolute;
  right: 0;
  bottom: 0;
  border-right: 2px solid #218c74;
  border-bottom: 2px solid #218c74;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  transition: 0.3s;
  opacity: 0;
}

.SMN_effect-24 a:before {
  width: 6px;
  height: 6px;
  background: transparent;
  content: "";
  position: absolute;
  right: 0;
  top: 0;
  border-right: 2px solid #218c74;
  border-top: 2px solid #218c74;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  transition: 0.3s;
  opacity: 0;
}

.SMN_effect-24 a:after {
  width: 6px;
  height: 6px;
  background: transparent;
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  border-left: 2px solid #218c74;
  border-bottom: 2px solid #218c74;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  transition: 0.3s;
  opacity: 0;
}

.SMN_effect-24 a:hover {
  color: #218c74;
}

.SMN_effect-24 a:hover:before {
  opacity: 1;
  right: 5px;
  top: 5px;
}

.SMN_effect-24 a:hover:after {
  opacity: 1;
  left: 5px;
  bottom: 5px;
}

.SMN_effect-24 a:hover span:before {
  opacity: 1;
  left: 5px;
  top: 5px;
}

.SMN_effect-24 a:hover span:after {
  opacity: 1;
  right: 5px;
  bottom: 5px;
}

.account {
  cursor: pointer;
  position: relative;
}

.account-dropdown {
  display: none;
  padding: 10px;
  background-color: rgba(0, 0, 0, 0.712);
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1;
  width: 100%;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  // transition: opacity 5s, max-height 5s ease-in-out;
  transition: all ease-in-out;
  opacity: 0;
  max-height: 0;
  overflow: hidden;
}

.account:hover .account-dropdown, .account-dropdown:hover{
  opacity: 1;
  display: block;
  max-height: 200px;
}
</style>
