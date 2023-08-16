package com.ysh.back.domain.portfolio.detail.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqPostPortfolioDetailPerchaseDTO {
    private Integer portfolioIdx;
    private String assetIdx;
    private BigDecimal amount;
    private BigDecimal averagePurchasePrice;
    private BigDecimal totalPurchasePrice;
    private String divedendMonth;
    private BigDecimal divendedAmount;
}
