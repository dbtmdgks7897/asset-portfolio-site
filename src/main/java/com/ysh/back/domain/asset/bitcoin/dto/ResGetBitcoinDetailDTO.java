package com.ysh.back.domain.asset.bitcoin.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResGetBitcoinDetailDTO {
    
    private String market;
    private BigDecimal opening_price;
    private BigDecimal high_price;
    private BigDecimal low_price;
    private BigDecimal trade_price;
    private BigDecimal prev_closing_price;
    private BigDecimal signed_change_price;
    private BigDecimal signed_change_rate;
}
