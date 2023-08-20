package com.ysh.back.domain.analyze.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.analyze.service.AnalyzeServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "자산 분석", description = "")
@RestController
@RequestMapping("/api/v1/portfolio/analyze")
public class AnalyzeControllerApiV1 {

    @Autowired
    AnalyzeServiceApiV1 analyzeServiceApiV1;


    @Operation(summary = "자산 분석 정보 받기", description = "현재 포트폴리오의 분석 정보 가져옴")
    @GetMapping("/{portfolioIdx}")
    public ResponseEntity<?> getAnalyzeData(
            @PathVariable Integer portfolioIdx,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return analyzeServiceApiV1.getAnalyzeData(portfolioIdx, customUserDetails);
    }

}
