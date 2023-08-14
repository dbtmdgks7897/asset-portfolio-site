package com.ysh.back.domain.asset.stock.domestic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqGetDomesticStockInfoDTO {
    private String FID_COND_MRKT_DIV_CODE;
    private String FID_INPUT_ISCD;
}
