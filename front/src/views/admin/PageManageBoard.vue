<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>게시물 관리</span>
        <div class="input-group mb-1">
          <input v-model="search"
            type="text"
            class="form-control"
            placeholder="검색어"
            aria-label="검색어"
            aria-describedby="button-addon2"
            @keydown.enter="searchFn"
          />
          <!-- 버튼 클릭 시 위의 인풋의 데이터를 -->
          <!-- 쿼리스트링 변수 search에 넣어서 -->
          <!-- /api/boards 로 보내기 -->
          <button
            class="btn btn-outline-secondary"
            type="button"
            id="button-addon2"
            @click="searchFn"
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
              <th scope="col">숨김 상태</th>
              <th scope="col">삭제일</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
              <tr v-for="board in boardList" :key="board">
                <th scope="row">{{ board.idx }}</th>
                <td>{{ util.truncateText(board.name, 15) }}</td>
                <td>{{ board.user.email }}</td>
                <td>{{ board.createdAt }}</td>
                <td>{{ board.viewCount }}</td>
                <td>{{ board.recommendCount }}</td>
                <td>{{ board.isHide }}</td>
                <td>{{ board.deletedAt }}</td>
                <td class="flex">
                  <button
                  v-if="!board.isHide"
                  class="btn my-red-button"
                  data-bs-toggle="modal"
                  data-bs-target="#exampleModal"
                  data-bs-whatever="@mdo"
                  @click="boardIdx = board.idx; disableButton()"
                >
                  <span>비</span>
                </button>
                <button
                  v-else
                  class="btn my-blue-button"
                  @click="boardIdx = board.idx; ableButton()"
                >
                  <span>활</span>
                </button>
                <!-- 탈퇴 버튼 -->
                <button
                  v-if="board.deletedAt == null"
                  class="btn my-button"
                  @click="boardIdx = board.idx; deleteButton()"
                >
                  <span>탈</span>
                </button>
                <button v-else class="btn my-blue-button"
                @click="boardIdx = board.idx; restoreButton()">
                  <span>복</span>
                </button>
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
import { util } from "@/utils/utils";
</script>
<script>
export default {
  data() {
    //변수생성
    return {
      trigger: true,
      boardList: null,
      search: null,
      boardIdx: null,
    };
  },
  mounted () {
    this.getBoardList();
  },
  methods: {
    changeTrigger(){
      alert('')
      this.trigger = !this.trigger;
    },
    getBoardList() {
      this.$axios
        .get(`/api/v1/admin/board`)
        .then((res) => {
          if (res.data.code === 0) {
            this.boardList = res.data.data.boardList;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    searchFn() {
      this.$axios
        .get(`/api/v1/admin/board`,{
          params: {
            search: this.search,
          }
        })
        .then((res) => {
          if (res.data.code === 0) {
            this.boardList = res.data.data.boardList;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    disableButton(){
      const hideReason = prompt(`게시물이 숨겨진 이유를 입력해주세요`)
      if(hideReason != null) {
        const dto = {
        hideReason: hideReason,
      };
      this.$axios
        .put(`/api/v1/admin/board/${this.boardIdx}/hide`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      }
      
    },
    ableButton(){
      this.$axios
        .put(`/api/v1/admin/board/${this.boardIdx}/hide`)
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    deleteButton(){
      const reason = prompt('삭제 사유를 입력해주세요.')
      if(reason != null){
        const dto = {
          reason : reason
        }
        this.$axios
        .put(`/api/v1/admin/board/${this.boardIdx}/deletedAt`, dto)
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      }
    },
    restoreButton(){
      this.$axios
        .put(`/api/v1/admin/board/${this.boardIdx}/deletedAt`)
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getBoardList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
};
</script>
<style lang="scss" scoped>
.contents {
  width: 90%;
  height: 100%;
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
