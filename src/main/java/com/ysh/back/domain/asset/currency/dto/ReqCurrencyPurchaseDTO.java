package com.ysh.back.domain.asset.currency.dto;

import java.math.BigDecimal;

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
public class ReqCurrencyPurchaseDTO {
    
    private Asset asset;
    private PortfolioDetail portfolioDetail;
    private Transaction transaction;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Asset{
        private String idx;
        private String name;
        private String type;
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class PortfolioDetail{
        private Integer portfolioIdx;
        private BigDecimal amount;
        private BigDecimal averagePurchasePrice;
        private BigDecimal totalPurchasePrice;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Transaction{
        private String type;
        private BigDecimal amount;
        private BigDecimal priceAvg;
        private BigDecimal profit;
    }
}
