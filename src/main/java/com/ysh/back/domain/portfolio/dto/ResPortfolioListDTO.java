package com.ysh.back.domain.portfolio.dto;

import java.util.List;

import org.checkerframework.checker.units.qual.s;

import com.ysh.back.domain.portfolio.dto.chart.ChartDataDTO;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResPortfolioListDTO {

    List<Portfolio> portfolioList;

    public static ResPortfolioListDTO of(List<PortfolioEntity> portfolioEntityList){
        return ResPortfolioListDTO.builder()
        .portfolioList(
            portfolioEntityList.stream()
            .map(portfolioEntity -> Portfolio.fromEntity(portfolioEntity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Portfolio {
        private Integer idx;
        private String name;
        private String description;
        private ChartDataDTO chartDataDTO;

        public static Portfolio fromEntity(PortfolioEntity portfolioEntity){
            return Portfolio.builder()
            .idx(portfolioEntity.getIdx())
            .name(portfolioEntity.getName())
            .description(portfolioEntity.getDescription())
            .build();
        }

        
    }

}
