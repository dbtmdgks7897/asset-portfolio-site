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
public class ReqPostPortfolioDetailSellDTO {
    private Integer portfolioIdx;
    private String assetIdx;
    private BigDecimal amount;
    private BigDecimal totalSellPrice;
}
