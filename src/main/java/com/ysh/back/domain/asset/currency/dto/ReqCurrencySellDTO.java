package com.ysh.back.domain.asset.currency.dto;

import java.math.BigDecimal;

import com.ysh.back.domain.asset.currency.dto.ReqCurrencyPurchaseDTO.PortfolioDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqCurrencySellDTO {
    
    private String assetIdx;
    private PortfolioDetail portfolioDetail;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class PorfolioDetail{
        private Integer portfolioIdx;
        private BigDecimal amount;
        private BigDecimal totalSellPrice;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class Transaction{
        private String type;
        private BigDecimal amount;
        private BigDecimal priceAvg;
    }
}
