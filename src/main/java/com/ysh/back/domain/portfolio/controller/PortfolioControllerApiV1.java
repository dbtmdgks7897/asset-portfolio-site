package com.ysh.back.domain.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.dto.ReqPortfolioInsertDTO;
import com.ysh.back.domain.portfolio.service.PortfolioServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "포트폴리오", description = "포트폴리오 도메인")
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioControllerApiV1 {
    
    @Autowired
    PortfolioServiceApiV1 portfolioServiceApiV1;

    @Operation(summary = "포트폴리오 생성", description = "포트폴리오 이름만 받아와 생성")
    @PostMapping("")
    public ResponseEntity<?> insertPortfolio(
        @Valid @RequestBody ReqPortfolioInsertDTO reqPortfolioInsertDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return portfolioServiceApiV1.insertPortfolio(reqPortfolioInsertDTO, customUserDetails);
    }
}
