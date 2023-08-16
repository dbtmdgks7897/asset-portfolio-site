<template>
  <div
    :class="[toggle.show ? 'sidebar-margin' : 'sidebar-margin-none']"
    class=""
  >
    <div class="contents">
      <div v-if="stockHead" class="contents-head flex">
        <span>{{ util.truncateText(stockHead.assetName, 12) }}</span>
        <span>({{ stockHead.assetCode }})</span>
      </div>
      <div v-if="stockData" class="contents-body table-responsive-xxl">
        <div class = "flex pricebox">
          <p class="price">{{ stockData.price }}</p>
          <p class="compare" :style="getPriceStyle(stockData.compareYester)"><span>{{ stockData.compareYester }}</span>({{ stockData.compareYesterRate }}%)</p>
        </div>
        <div class = "pricebox">
          <p style="color: red"><span>금일 최고가</span> | {{ stockData.highPrice }}</p>
          <p style="color: blue"><span>금일 최저가</span> | {{ stockData.rowPrice }}</p>
        </div>
      </div>
    </div>
  </div>
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
      stockHead: null,
      stockData: {
        price: null,
        compareYester: null,
        compareYesterRate: null,
        highPrice: null,
        rowPrice: null,
      },
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
    bookmarking() {
      this.$axios.post(`/`);
    },
    assetRegistration() {
      this.$axios
        .get(
          `/api/v1/asset/stock/domestic/${this.stockCode}`,
          {
            params: {
              stockType: this.stockType,
            },
          },
          {
            headers: {
              "Content-Type": "application/json;charset=utf-8",
            },
          }
        )
        .then((res) => {
          if (res.data.code === 0) {
            this.stockHead = res.data.data;
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
            const data = event.data.split("|");
            const signal = data[0];
            if (signal === "0" || signal === "1") {
              const tempStockData = data[3].split("^");
              this.stockData.price = tempStockData[2];
              this.stockData.compareYester = tempStockData[4];
              this.stockData.compareYesterRate = tempStockData[5];
              this.stockData.highPrice = tempStockData[8];
              this.stockData.rowPrice = tempStockData[9];

              console.log(tempStockData);
            }
            // console.log(filterUnicode(data));
            // console.log(data);
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
    getPriceStyle(value) {
      const numValue = parseFloat(value);
      const style = {};

      if (numValue > 0) {
        style.color = "red"; // 양수일 경우 초록색 글자
      } else if (numValue < 0) {
        style.color = "blue"; // 음수일 경우 빨간색 글자
      } else {
        style.color = "black"; // 0일 경우 검정색 글자
      }

      return style;
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
    span {
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
    .pricebox{
        align-items: center;
        .price{
            font-size: 4vw;
        }
        p{
            font-size: 2vw;
            margin-left: 20px;
        }
    }
  }
}
</style>
