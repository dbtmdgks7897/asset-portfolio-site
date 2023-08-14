package com.ysh.back.domain.asset.stock.domestic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResDomesticStockInfoDTO {
    @JsonProperty("rt_cd")
    private String rtCd;
    public String msg_cd;
    private String msg1;
    private Output output;
    
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Output{
        private String bstp_kor_isnm;
        private String stck_prpr;
        private String prdy_vrss;
        private String prdy_ctrt;
    }
}
