package com.ysh.back.domain.asset.currency.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
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
    
    @NotNull(message = "입력된 자산 정보가 잘못되었습니다.")
    private Asset asset;
    @NotNull(message = "입력된 포트폴리오 정보가 잘못되었습니다.")
    private PortfolioDetail portfolioDetail;
    @NotNull(message = "입력된 거래 정보가 잘못되었습니다.")
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
