package com.ysh.back.domain.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.dto.ReqPortfolioInsertDTO;
import com.ysh.back.domain.portfolio.service.PortfolioServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "포트폴리오", description = "포트폴리오 도메인")
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioControllerApiV1 {
    
    @Autowired
    PortfolioServiceApiV1 portfolioServiceApiV1;

    @Operation(summary = "내 포트폴리오 리스트", description = "현재 유저 idx로 생성된 포트폴리오 리스트 가져옴")
    @GetMapping()
    public ResponseEntity<?> getPortfolioListData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return portfolioServiceApiV1.getPortfolioListData(customUserDetails);
    }

    @Operation(summary = "포트폴리오 생성", description = "포트폴리오 이름만 받아와 생성")
    @PostMapping()
    public ResponseEntity<?> insertPortfolioData(
        @Valid @RequestBody ReqPortfolioInsertDTO reqPortfolioInsertDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return portfolioServiceApiV1.insertPortfolioData(reqPortfolioInsertDTO, customUserDetails);
    }

    @Operation(summary = "포트폴리오 삭제", description = "포트폴리오 삭제")
    @DeleteMapping("/{portfolioIdx}")
    public ResponseEntity<?> deletePortfolioData(
        @PathVariable Integer portfolioIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        System.out.println();
        return portfolioServiceApiV1.deletePortfolioData(portfolioIdx, customUserDetails);
    }
}
