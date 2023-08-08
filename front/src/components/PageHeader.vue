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
            <div class="profile flex">
              <img
                v-if="login.isLogined"
                class="flex-item"
                :src="profile.img"
                alt="프로필 이미지"
              />
              <span v-if="login.isLogined" class="flex-item name">{{
                profile.name
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
            </div>
          </a>
        </li>
        <br />
        <!-- 나중엔 -->

        <!-- 포트폴리오 카테고리 -->
        <!-- 로그인 전엔 안보임 -->
        <div v-if="!isSidebarHide">
          <li class="list-group-item list-group-item-dark category">자산</li>
          <li class="list-group-item list-group-item-dark">내 포트폴리오</li>
          <li class="list-group-item list-group-item-dark" @click="this.$router.push('/asset')">자산 검색</li>
          <li class="list-group-item list-group-item-dark">분석</li>
          <li class="list-group-item list-group-item-dark" @click="this.$router.push('/asset/transaction')">거래내역</li>
        </div>
        <div v-else>
          <li class="list-group-item list-group-item-dark category">자산</li>
        </div>

        <br />
        <!-- 내 계정 카테고리 -->
        <!-- 로그인 전엔 안보임 -->
        <div v-if="!isSidebarHide">
          <li class="list-group-item list-group-item-dark category">내 계정</li>
          <li
            class="list-group-item list-group-item-dark"
            @click="this.$router.push('/myinfo')"
          >
            내 정보
          </li>
          <li
            class="list-group-item list-group-item-dark"
            @click="this.$router.push('/myactive')"
          >
            내 활동
          </li>
          <li class="list-group-item list-group-item-dark" @click="logout">로그아웃</li>
        </div>
        <div v-else>
          <li class="list-group-item list-group-item-dark category" @click="this.$router.push('/myinfo')">내 계정</li>
        </div>
        <br />
        <!-- 커뮤니티 카테고리 -->
        <!-- 로그인 안해도 보임 -->
        <div v-if="!isSidebarHide">
          <li class="list-group-item list-group-item-dark category">
            커뮤니티
          </li>
          <li
            class="list-group-item list-group-item-dark"
            @click="this.$router.push('/board')"
          >
            자유 게시판
          </li>
        </div>
        <div v-else><li class="list-group-item list-group-item-dark category" @click="this.$router.push('/board')">
            커뮤니티
          </li></div>
        <br />

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
            유저 관리
          </li>
          <li
            class="list-group-item list-group-item-dark"
            @click="this.$router.push('/admin/board')"
          >
            게시물 관리
          </li>
        </div>
        <div v-else><li class="list-group-item list-group-item-dark category"  @click="this.$router.push('/admin/user')">
            관리
          </li></div>
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
      profile: {
        name: "anonymous",
        img: require("../assets/img/anonymous.png"),
      },
    };
  },
  methods: {
    profileClick() {
      // 현재 로그인 상태를 가정하여, 조건에 따라 라우터 이동
       // 로그인 상태라면 true, 로그인하지 않은 상태라면 false로 변경
      if (login.isLogined) {
        // this.isActive = !this.isActive; // 로그인 상태에서 클릭 시 내 정보 페이지로 이동
        // this.$router.push("/"); // 실제 라우터 이동이 필요한 경우 주석 해제
        this.$router.push({name : "PageMyinfo"})
      } else {
        // this.$router.push('/login'); // 실제 라우터 이동이 필요한 경우 주석 해제
        this.$router.push({name : "PageLogin"})
      }
    },
    logout(){
      login.isLogined = false;
      this.$router.push({name: "PageBoardList"})
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
  font-size: 1.5vw;
  text-align: left;
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

@media(max-width: 767px) {
  .profile{
    .name{
      display: none;
    }
  }
}

.list-group-item {
  background-color: rgba(255, 255, 255, 0);
  border: 0px;
  color: white;
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
</style>
