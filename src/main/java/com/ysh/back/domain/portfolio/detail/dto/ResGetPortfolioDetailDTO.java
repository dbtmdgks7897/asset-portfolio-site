package com.ysh.back.domain.portfolio.detail.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.sound.sampled.Port;

import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResGetPortfolioDetailDTO {

    List<PortfolioDetail> detailList;

    public static ResGetPortfolioDetailDTO of(List<PortfolioDetailEntity> entityList){
        return ResGetPortfolioDetailDTO.builder()
        .detailList(
            entityList.stream()
            .map((entity) -> PortfolioDetail.fromEntity(entity))
            .toList()
        )
        .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class PortfolioDetail {
        private String code;
        private String name;
        private BigDecimal curPrice;
        private BigDecimal purchasePrice;
        private BigDecimal amount;
        private BigDecimal profit;
        private String dividendMonth;
        private BigDecimal dividendAmount;

        public static PortfolioDetail fromEntity(PortfolioDetailEntity entity) {
            PortfolioDetail portfolioDetail = PortfolioDetail.builder()
                    .code(entity.getAssetEntity().getIdx())
                    .name(entity.getAssetEntity().getName())
                    .curPrice(entity.getAssetEntity().getPrice())
                    .purchasePrice(entity.getAveragePurchasePrice())
                    .amount(entity.getAmount())
                    .build();
            if(entity.getAssetEntity().getPrice() != null){
                portfolioDetail.setProfit(entity.getAmount()
                            .multiply(entity.getAssetEntity().getPrice().subtract(entity.getAveragePurchasePrice())));
            }
            if(entity.getDividendMonth() != null && entity.getDividendAmount() != null){
                portfolioDetail.setDividendMonth(entity.getDividendMonth());
                portfolioDetail.setDividendAmount(entity.getDividendAmount());
            }
            return portfolioDetail;
        }
    }

}
