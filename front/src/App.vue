<template>
  <PageHeader v-if="!noSidebarComponentsName.includes($route.name)" />
  <router-view />
  <PageFooter v-if="!noFooterComponentsName.includes($route.name)" />
</template>

<script>
import PageHeader from "./components/PageHeader";
import PageFooter from "./components/PageFooter";
// import MainHeader from './components/MainHeader';

export default {
  name: "App",

  data() {
    return {
      noSidebarComponentsName: [
        'PageMain', 'PageJoin', 'PageLogin'
      ],
      noFooterComponentsName: [
        'PageMain', 'PageJoin', 'PageLogin', 'PageMyinfo', 'PageMyactive', 'PageMyPortfolio'
      ],
      isLogin: true,
    };
  },

  components: {
    PageFooter,
    PageHeader,
    // MainHeader
  },
  methods: {},
};
</script>

<style lang="scss">
#app {
  font-family: SUITE-Regular, Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
  margin: 0px;
}

@font-face {
  font-family: "SUITE-Regular";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2304-2@1.0/SUITE-Regular.woff2")
    format("woff2");
  font-weight: 400;
  font-style: normal;
}

@mixin my-button () {
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
</style>
