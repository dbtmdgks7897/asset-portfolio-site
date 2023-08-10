<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div class="contents-head flex">
        <span>유저 관리</span>
        <div class="input-group mb-1">
          <input
            v-model="search"
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
            @click="searchFn"
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
              <th scope="col" colspan="4">프로필</th>
              <th scope="col">가입일</th>
              <th scope="col">나이</th>
              <th scope="col">성별</th>
              <th scope="col">닉네임</th>
              <th scope="col">전화번호</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <!-- v-for로 반복 돌려서 데이터 가져와서 링크 넣고 뿌려주기 -->
            <tr v-for="user in userList" :key="user">
              <th scope="row">{{ user.idx }}</th>
              <td><img :src="user.profileImg" alt="이미지" /></td>
              <td>{{ user.email }}</td>
              <td>{{ user.name }}</td>
              <td>{{ user.createdAt }}</td>
              <td>{{ user.age }}</td>
              <td>{{ user.gender }}</td>
              <td>{{ user.nickname }}</td>
              <td>{{ user.phone }}</td>
              <td class="flex">
                <!-- 비활성화 / 활성화 버튼, 회원의 suspend_until 컬럼으로 상태 파악 -->
                <button
                  v-if="user.suspendUntil == null"
                  class="btn my-red-button"
                  data-bs-toggle="modal"
                  data-bs-target="#exampleModal"
                  data-bs-whatever="@mdo"
                  @click="suspendUserIdx = user.idx"
                >
                  <span>비</span>
                </button>
                <button
                  v-else
                  class="btn my-blue-button"
                  @click="dissuspendUserIdx = user.idx; dissuspendButton()"
                >
                  <span>활</span>
                </button>
                <!-- 탈퇴 버튼 -->
                <button
                  v-if="user.deletedAt == null"
                  class="btn my-button"
                  @click="deleteUserIdx = user.idx; deleteButton()"
                >
                  <span>탈</span>
                </button>
                <button v-else class="btn my-blue-button"
                @click="restoreUserIdx = user.idx; restoreButton()">
                  <span>복</span>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">New message</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form>
            <div class="mb-3">
              <label for="recipient-name" class="col-form-label"
                >정지 기간(일):</label
              >
              <input
                v-model="suspendDuration"
                type="text"
                class="form-control"
                id="recipient-name"
              />
            </div>
            <div class="mb-3">
              <label for="message-text" class="col-form-label"
                >정지 사유:</label
              >
              <textarea
                v-model="suspendReason"
                class="form-control"
                id="message-text"
              ></textarea>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button
            @click="suspendButton"
            data-bs-toggle="modal"
            type="button"
            class="btn btn-primary"
          >
            Send message
          </button>
        </div>
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
      userList: null,
      search: null,
      suspendUserIdx: null,
      dissuspendUserIdx: null,
      suspendDuration: null,
      suspendReason: null,
      deleteUserIdx: null,
      restoreUserIdx: null,
    };
  },
  mounted() {
    this.getUserList();
  },
  methods: {
    changeTrigger(id) {
      console.log(id);
      this.trigger = !this.trigger;
    },
    getUserList() {
      this.$axios
        .get(`/api/v1/admin/user`)
        .then((res) => {
          if (res.data.code === 0) {
            this.userList = res.data.data.userList;

            this.userList.forEach((element) => {
              let blob = new Blob([new ArrayBuffer(element.profileImg)], {
                type: "image/" + element.imgType,
              });
              const url = window.URL.createObjectURL(blob);
              element.profileImg = url;
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    searchFn() {
      this.$axios
        .get(`/api/v1/admin/user`, {
          params: {
            search: this.search,
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            this.userList = res.data.data.userList;

            this.userList.forEach((element) => {
              let blob = new Blob([new ArrayBuffer(element.profileImg)], {
                type: "image/" + element.imgType,
              });
              const url = window.URL.createObjectURL(blob);
              element.profileImg = url;
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    suspendButton() {
      const dto = {
        suspendDuration: this.suspendDuration,
        suspendReason: this.suspendReason,
      };
      this.$axios
        .put(`/api/v1/admin/user/${this.suspendUserIdx}/suspend`, dto, {
          headers: {
            "Content-Type": "application/json;charset=utf-8;",
          },
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getUserList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    dissuspendButton() {
      if (confirm(`${this.dissuspendUserIdx}번 유저를 활성화 하시겠습니까?`)) {
        this.$axios
          .put(`/api/v1/admin/user/${this.dissuspendUserIdx}/suspend`, {
            headers: {
              "Content-Type": "application/json;charset=utf-8;",
            },
          })
          .then((res) => {
            if (res.data.code === 0) {
              alert(res.data.message);
              this.getUserList();
            } else {
              alert(res.data);
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    deleteButton() {
      const reason = prompt('해당 유저의 정지 사유를 입력해주세요');
      const dto = {
        reason : reason
      }
      if(reason != ""){
        console.log(reason)
        this.$axios
        .post(`/api/v1/admin/user/${this.deleteUserIdx}/deletedAt`, dto, {
          headers: {
            'Content-Type' : 'application/json;charset=utf-8;'
          }
        })
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getUserList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      }
    },
    restoreButton() {
      this.$axios
        .post(`/api/v1/admin/user/${this.restoreUserIdx}/deletedAt`)
        .then((res) => {
          if (res.data.code === 0) {
            alert(res.data.message);
            this.getUserList();
          } else {
            alert(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
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

.my-button {
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
