package com.ysh.back.domain.asset.stock.domestic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.stock.domestic.service.DomesticStockServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "국내 주식 정보", description = "국내 주식 정보")
@RestController
@RequestMapping("/api/v1/asset/stock/domestic")
public class DomesticStockControllerApiv1 {

    @Autowired
    DomesticStockServiceApiV1 domesticStockServiceApiV1;

    @Operation(summary = "국내 주식 검색", description = "주식 코드로 검색")
    @GetMapping()
    public ResponseEntity<?> getDomesticStockInfoData(@RequestParam String stockCode,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return domesticStockServiceApiV1.getDomesticStockInfoData(stockCode, customUserDetails);
    }

    @Operation(summary = "국내 주식 상세정보", description = "data.go.kr에서 주식 이름 불러오기 \n" +
    "KIS에서 웹소켓을 받아와 실시간 주식 정보를 불러옴, 장외 시간엔 실시간X")
    @GetMapping("/{stockCode}")
    public ResponseEntity<?> getDomesticStockDetailData(
            @PathVariable String stockCode,
            @RequestParam("stockType") String stockType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return domesticStockServiceApiV1.getDomesticStockDetailData(stockCode, stockType, customUserDetails);
    }

}
