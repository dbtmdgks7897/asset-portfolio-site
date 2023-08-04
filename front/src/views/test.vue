<template>
  <div :class="{test: true, a: a}">
    {{ a }}
    what?
  </div>
</template>

<script setup>
// import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  name: "TestPage",

  data() {
    //변수생성
    return {
      a: false,
    };
  },
  updated() {
    console.log(this.a);
    this.a = true;
  },
  methods: {},
};
</script>
<style lang="scss" scoped>
.test {
  width: 500px;
  height: 500px;
  background-color: black;
  opacity: 0;
  transition: all 1s;
}

.a{
  opacity: 1;
  transition: all;
  transition-delay: 1s;
  transition-duration: 5s;
}

.fade-enter-active {
  animation: bounce-in 0.5s;
}
.fade-leave-active {
  animation: bounce-in 0.5s reverse;
}

@keyframes bounce-in {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.25);
  }
  100% {
    transform: scale(1);
  }
}
</style>
