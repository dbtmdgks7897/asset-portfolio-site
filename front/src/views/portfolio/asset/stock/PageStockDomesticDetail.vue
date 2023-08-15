<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div v-if="stockData" class="contents-head flex">
        <span>{{ util.truncateText(stockData.assetName,12)}}</span>
        <span>({{stockData.assetCode}})</span>
      </div>
      <div class="contents-body table-responsive-xxl">
        <!-- 검색했냐? -->
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

	// /*** Start	unicode replace step ********************************************************/	
	// // eslint-disable-next-line no-control-regex
	// var escapable = /[\x00-\x1f\ud800-\udfff\u200c\u2028-\u202f\u2060-\u206f\ufff0-\uffff]/g;
	// function filterUnicode(quoted)	{
	// 	escapable.lastIndex = 0;
	// 	if (!escapable.test(quoted))	return quoted;
		
	// 	return quoted.replace (escapable, function(){
	// 		return '';
	// 	});
	// }
	// /** End of unicode replace step ********************************************************/


export default {
  data() {
    //변수생성
    return {
      /** @type {WebSocket} */
      socket: null,
      isUserSearching: true,
      stockCode: this.$route.params.stockCode,
      stockType: "주식-국내",
      stockData: null,
      approvalKey: null,
    };
  },
  mounted() {
    this.assetRegistration();
    this.connectToWebSocket();
  },

  methods: {
    userSearch(id) {
      console.log(id);
      this.isUserSearching = !this.isUserSearching;
    },
    bookmarking(){
      this.$axios
      .post(`/`)
    },
    assetRegistration(){
      const dto = {
        assetCode: this.stockCode,
        assetType: this.stockType
      }
      this.$axios
      .post(`/api/v1/asset`, dto,{
        headers : {
          'Content-Type' : 'application/json;charset=utf-8'
        }
      }).then((res) => {
          if (res.data.code === 0) {
            this.stockData = res.data.data;
          } else {
            alert(res.data.message);
          }
        })
        .catch((err) => {
          alert(err.response.data.message);
        });
    },
    // getDomesticStockDetailData() {
    //   this.$axios
    //     .get("/api/v1/asset/stock/domestic/" + this.stockCode, {
    //       params: {
    //         stockCode: this.stockCode,
    //       },
    //     })
    //     .then((res) => {
    //       if (res.data.code === 0) {
    //         this.stockData = res.data.data;
    //       } else {
    //         alert(res.data.message);
    //       }
    //     })
    //     .catch((err) => {
    //       alert(err.response.data.message);
    //     });
    // },
    async connectToWebSocket() {
      try {
        const response = await this.$axios.post("/api/v1/apiAuth/approval");
        this.approvalKey = response.data;

        if (this.approvalKey) {
          this.socket = new WebSocket(
            `ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0?approval_key=${this.approvalKey}`
          );

          this.socket.onopen = () => {
            console.log("Connected to WebSocket");
            this.sendStockInfo();
          };

          this.socket.onmessage = async (event) => {
            const data = JSON.parse(event.data);
            // console.log(filterUnicode(data));
            console.log(data);
            // 추가로 필요한 데이터 처리 코드를 여기에 작성하세요.

            // 연결 성공시 할일있으면 처리 / 없으면 무시
            // 실제 데이터가 날아오면 data.split("^");
            // PINGPONG 처리

            // if(data.header.tr_id == "PINGPONG"){
            //   await this.socket.send(data);
            // }


          };

          this.socket.onerror = (error) => {
            console.error("WebSocket Error", error);
          };

          this.socket.onclose = (event) => {
            console.log("WebSocket closed", event);
          };
        }
      } catch (error) {
        console.error("Error fetching approval key:", error);
      }
    },
    sendStockInfo() {
      const message = {
        header: {
          approval_key: this.approvalKey,
          custtype: "P",
          tr_type: "1",
          "content-type": "utf-8",
        },
        body: {
          input: {
            tr_id: "H0STCNT0",
            tr_key: this.stockCode, // 이 부분은 원하는 종목 코드로 변경해야 합니다.
          },
        },
      };

      this.socket.send(JSON.stringify(message));
    },
  },
};
</script>
<style lang="scss" scoped>
body {
  background-color: black;
}
.contents {
  background-color: #fff;
  &-head {
    span{
        font-size: 3vw;
      }
    div {
      width: 20vw;
      height: 50%;
      
    }
  }
  &-body {
    width: 100%;
    height: 70%;
    background-color: blueviolet;
  }
}
</style>
