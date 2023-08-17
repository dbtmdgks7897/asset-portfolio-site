package com.ysh.back.domain.transaction.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqPostTransactionDTO {
    private Integer portfolioIdx;
    private String assetIdx;
    private String type;
    private BigDecimal amount;
    private BigDecimal priceAvg;
    private BigDecimal profit;
}
