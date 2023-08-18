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

    @Operation(summary = "내 포트폴리오 리스트", description = "현재 유저 idx로 생성된 포트폴리오 리스트 가져옴")
    @GetMapping("/{portfolioIdx}")
    public ResponseEntity<?> getDetailCharts(
        @PathVariable Integer portfolioIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return portfolioDetailServiceApiV1.getDetailChart(portfolioIdx, customUserDetails);
    }
}
