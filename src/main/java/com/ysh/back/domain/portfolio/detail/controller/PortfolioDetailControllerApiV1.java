package com.ysh.back.domain.portfolio.detail.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.detail.service.PortfolioDetailServiceApiV1;
import com.ysh.back.domain.portfolio.dto.chart.ChartDataDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "포트폴리오 상세", description = "포트폴리오 상세 도메인")
@RestController
@RequestMapping("/api/v1/portfolio/detail")
public class PortfolioDetailControllerApiV1 {

    @Autowired
    PortfolioDetailServiceApiV1 portfolioDetailServiceApiV1;

    @Operation(summary = "상세 차트", description = "각 자산 별 상세 자산들의 차트")
    @GetMapping("/{portfolioIdx}/chart")
    public ResponseEntity<?> getDetailCharts(
        @PathVariable Integer portfolioIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return portfolioDetailServiceApiV1.getDetailChart(portfolioIdx, customUserDetails);
    }
    
    @Operation(summary = "상세 리스트", description = "상세 자산 리스트")
    @GetMapping("/{portfolioIdx}/list")
    public ResponseEntity<?> getDetailList(
        @PathVariable Integer portfolioIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return portfolioDetailServiceApiV1.getDetailList(portfolioIdx, customUserDetails);
    }
}
