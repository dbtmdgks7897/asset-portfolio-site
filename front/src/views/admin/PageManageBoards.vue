<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>게시물 관리</span>
        <div class="input-group mb-1">
          <input
            type="text"
            class="form-control"
            placeholder="검색어"
            aria-label="검색어"
            aria-describedby="button-addon2"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/boards 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
          >
            <i class="bi bi-search"></i>
          </button>
        </div>
      </div>
      <div class="contents-body table-responsive-xxl">
        <table class="table">
          <thead class="table-dark">
            <tr>
              <th scope="col">idx</th>
              <th scope="col">이름</th>
              <th scope="col">작성자</th>
              <th scope="col">등록일</th>
              <th scope="col">조회수</th>
              <th scope="col">추천수</th>
              <th scope="col">상태</th>
              <th scope="col">삭제일</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr>
                <th scope="row">(post.idx)</th>
                <td>(post.name)</td>
                <td>(post.user_idx)</td>
                <td>(post.created_at)</td>
                <td>(post.view_count)</td>
                <td>(post.recommend_count)</td>
                <td>(post.isHide)</td>
                <td>(post.deleted_at)</td>
                <td class="flex">
                  <button v-if="trigger" class="btn my-red-button" @click="changeTrigger()"><span>비</span></button>
                  <button v-else class="btn my-blue-button" @click="changeTrigger()"><span>활</span></button>
                  <button class="btn my-button" @click="exileButton"><span>탈</span></button>
                </td>
              </tr>
          </tbody>
        </table>
      </div>
    </div>  
</div>
  <!-- 로그인 시에만 보이게 -->
</template>

<script setup>
import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  data() {
    //변수생성
    return {
      trigger: true,
    };
  },
  methods: {
    changeTrigger(){
      alert('')
      this.trigger = !this.trigger
    },
    exileButton(){
      alert('')
    }
  },
};
</script>
<style lang="scss" scoped>
.contents {
  width: 90%;
  &-head {
    div {
      width: 20vw;
      height: 50%;
    }
  }
}

.my-button{
  width: 2vw;
  height: 3vh;
  font-size: 1vw;
}
.my-red-button {
    $main-color: red;
    $sub-color: white;

    width: 2vw;
    height: 3vh;
    font-size: 1vw;
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

.my-blue-button {
    $main-color: blue;
    $sub-color: white;

    width: 2vw;
    height: 3vh;
    font-size: 1vw;
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



</style>
