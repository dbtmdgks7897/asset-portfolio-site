<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>자유 게시판</span>
        <select
          class="form-select form-select-sm"
          aria-label="Small select example"
        >
          <option selected>정렬 기준</option>
          <option value="1">(post.idx)</option>
          <option value="2">(post.created_at)</option>
          <option value="3">(post.view_count)</option>
          <!-- post에서 oneToMany 매핑으로 -->
          <!-- 행의 수 합 -->
          <option value="4">(SUM(post-recommend.post_idx))</option>
        </select>
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
              <th scope="col">번호</th>
              <th scope="col">게시물 제목</th>
              <th scope="col">작성자</th>
              <th scope="col">등록일</th>
              <th scope="col">조회수</th>
              <th scope="col">추천</th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr
              @click="
                this.$router.push({
                  name: 'PageBoardDetail',
                  params: { id: 2 },
                })
              "
            >
              <th scope="row">(post.idx)</th>
              <td>(post.title)</td>
              <td>(post.user_idx)</td>
              <td>(post.created_at)</td>
              <td>(post.view_count)</td>
              <td>(SUM(post-recommend.post_idx))</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <!-- 로그인 시에만 보이게 -->
  <button
    class="btn btn btn-outline-dark posting"
    @click="this.$router.push('/boards/write')"
  >
    <i class="bi bi-file-earmark-plus"></i>
  </button>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
</script>
<script>
export default {
  name: "PageBoardList",
  data() {
    //변수생성
    return {};
  },
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.contents {
  &-head {
    span {
      font-size: 3vw;
    }

    select {
      width: 10vw;
      height: 100%;
    }

    div {
      width: 20vw;
      height: 50%;

      input {
        font-size: 1vw;
      }

      button {
        width: 2vw;
        padding: 0;
        text-align: center;

        i {
          font-size: 1vw;
        }
      }
    }
  }
  &-body {
    width: 100%;
  }
}

.posting {
  position: fixed;
  left: 90%;
  top: 85%;
  width: 7vw;
  height: 7vw;
  border-radius: 100%;
  border-color: blueviolet;
  &:hover {
    background-color: white;
    border-color: rgb(61, 16, 102);
  }
  i {
    text-align: center;
    font-size: 2vw;
    color: blueviolet;
    border-color: rgb(61, 16, 102);
  }
}

@media (max-width: 767px) {
  .contents {
    width: 100%;
  }
}
</style>
