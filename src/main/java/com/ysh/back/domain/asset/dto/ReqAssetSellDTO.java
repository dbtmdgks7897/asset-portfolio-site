package com.ysh.back.domain.asset.dto;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqAssetSellDTO {
    
    private String assetIdx;
    private PortfolioDetail portfolioDetail;
    private Transaction transaction;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class PortfolioDetail{
        private Integer portfolioIdx;
        private BigDecimal amount;
        private BigDecimal totalSellPrice;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Transaction{
        private String type;
        private BigDecimal amount;
        private BigDecimal priceAvg;
    }
}
