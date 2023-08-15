package com.ysh.back.domain.asset.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiGetDomesticStockNameDTO {
    private Response response;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response{
        private Header header;
        private Body body;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        @Builder
        public static class Header{
            private String resultCode;
            private String resultMsg;

        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        @Builder
        public static class Body{
            private Items items;

            @NoArgsConstructor
            @AllArgsConstructor
            @Getter
            @Setter
            @Builder
            public static class Items{
                private List<Item> item;


                @NoArgsConstructor
                @AllArgsConstructor
                @Getter
                @Setter
                @Builder
                public static class Item{
                    private String itmsNm;
                }
                
            }
        }

    }
}
