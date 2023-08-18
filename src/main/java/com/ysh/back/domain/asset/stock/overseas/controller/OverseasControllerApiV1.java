package com.ysh.back.domain.asset.stock.overseas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.stock.overseas.service.OverseasServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "해외 주식 정보", description = "해외 주식 정보")
@RestController
@RequestMapping("/api/v1/asset/stock/overseas")
public class OverseasControllerApiV1 {
    
    @Autowired
    OverseasServiceApiV1 overseasServiceApiV1;

    


    @Operation(summary = "해외 주식 api키", description = "주식 코드로 검색")
    @GetMapping("/apikey")
    public String getOverseasApi(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return overseasServiceApiV1.getApikey(customUserDetails);
    }
}
