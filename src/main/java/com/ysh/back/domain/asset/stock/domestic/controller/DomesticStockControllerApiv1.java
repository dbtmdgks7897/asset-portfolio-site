package com.ysh.back.domain.asset.stock.domestic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.stock.domestic.service.DomesticStockServiceApiV1;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "국내 주식 정보", description = "국내 주식 정보")
@RestController
@RequestMapping("/api/v1/asset/stock/domestic")
public class DomesticStockControllerApiv1 {

    @Autowired
    DomesticStockServiceApiV1 domesticStockServiceApiV1;

    @GetMapping()
    public ResponseEntity<?> getStockInfoData(@RequestParam String stockCode,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return domesticStockServiceApiV1.getStockInfoData(stockCode, customUserDetails);
    }

    @GetMapping("/{stockCode}")
    public ResponseEntity<?> getStockDetailData(
            @PathVariable String stockCode,
            @RequestParam("stockType") String stockType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return domesticStockServiceApiV1.getStockDetailData(stockCode, stockType, customUserDetails);
    }

}
