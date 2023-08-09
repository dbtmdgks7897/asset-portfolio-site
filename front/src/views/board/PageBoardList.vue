<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>자유 게시판</span>
        <select
          v-model="sort"
          class="form-select form-select-sm"
          aria-label="Small select example"
        >
          <option value="idx" selected>번호</option>
          <option value="created_at">등록일</option>
          <option value="view_count">조회수</option>
          <!-- post에서 oneToMany 매핑으로 -->
          <!-- 행의 수 합 -->
          <option value="recommend_count">추천수</option>
        </select>

        <div class="form-check">
          <input
            v-model="desc"
            class="form-check-input"
            type="checkbox"
            value="true"
            id="flexCheckIndeterminate"
          />
          내림차순
        </div>
        <div class="input-group mb-1">
          <input
            v-model="search"
            type="text"
            class="form-control"
            placeholder="검색어"
            aria-label="검색어"
            aria-describedby="button-addon2"
            @keydown.enter="getBoardListFn"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/board 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
            @click="getBoardListFn()"
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
              v-for="board in chunkedBoardList[pageNum - 1]"
              :key="board.idx"
              @click="
                this.$router.push({
                  name: 'PageBoardDetail',
                  params: { id: board.idx },
                })
              "
            >
              <th scope="row">{{ board.idx }}</th>
              <td>{{ util.truncateText(board.name, 25) }}</td>
              <td>{{ board.user.name }}</td>
              <td>{{ board.createdAt }}</td>
              <td>{{ board.viewCount }}</td>
              <td>{{ board.recommendCount }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <nav class="flex" aria-label="...">
        <ul class="pagination">
          <li v-if="pageNum - 1 > 0" class="page-item pre">
            <a
              class="page-link"
              tabindex="-1"
              aria-disabled="true"
              @click="pageNum -= 1"
              >Previous</a
            >
          </li>
          <li v-if="pageNum - 1 > 0" class="page-item">
            <a class="page-link" @click="pageNum -= 1">{{ pageNum - 1 }}</a>
          </li>
          <li class="page-item active" aria-current="page">
            <a class="page-link" href="#">{{ pageNum }}</a>
          </li>
          <li v-if="pageNum + 1 <= chunkedBoardList.length" class="page-item">
            <a class="page-link" @click="pageNum += 1">{{ pageNum + 1 }}</a>
          </li>
          <li
            v-if="pageNum + 1 <= chunkedBoardList.length"
            class="page-item next"
          >
            <a class="page-link" @click="pageNum += 1">Next</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>

  <!-- 로그인 시에만 보이게 -->
  <button
    v-if="login.isLogined"
    class="btn btn btn-outline-dark posting"
    @click="this.$router.push('/board/write')"
  >
    <i class="bi bi-file-earmark-plus"></i>
  </button>
</template>

<script setup>
import { toggle } from "@/utils/toggle";
import { login } from "@/utils/login";
import { util } from "@/utils/utils";
</script>
<script>
export default {
  name: "PageBoardList",
  data() {
    //변수생성
    return {
      boardList: [],
      search: null,
      sort: "idx",
      desc: true,
      chunkSize: 15,
      pageNum: 1,
    };
  },
  mounted() {
    this.getBoardListFn();
  },
  computed: {
    chunkedBoardList() {
      const chunkSize = this.chunkSize;
      const result = [];
      for (let i = 0; i < this.boardList.length; i += chunkSize) {
        result.push(this.boardList.slice(i, i + chunkSize));
      }
      return result;
    },
  },
  // updated :{
  //   test() {
  //     console.log(this.pageNum);
  //   }
  // },
  watch: {
    sort() {
      this.getBoardListFn();
    },
    desc() {
      this.getBoardListFn();
    },
  },
  methods: {
    getBoardListFn() {
      this.$axios
        .get(`/api/v1/board`, {
          params: {
            search: this.search,
            sort: this.sort,
            desc: this.desc,
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            console.log(res.data.message);
            this.boardList = res.data.data.boardList;
            this.pageNum = 1;
          }
        })
        .catch((res) => {
          console.error(res);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.contents {
  height: 100%;
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

.form-check {
  width: 5vw !important;
  font-size: 1vw;
}

nav {
  justify-content: center;
}
</style>
