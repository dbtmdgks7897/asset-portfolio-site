package com.ysh.back.domain.asset.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.bitcoin.service.BitcoinServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "비트코인", description = "비트코인 도메인")
@RestController
@RequestMapping("/api/v1/asset/bitcoin")
public class BitcoinControllerApiV1 {

    @Autowired
    BitcoinServiceApiV1 bitcoinServiceApiV1;
    
    @Operation(summary = "비트코인 상세 정보", description = "외부 api에서 불러옴")
    @GetMapping("/{bitcoinCode}")
    public ResponseEntity<?> getBitcoinDetail(
        @PathVariable String bitcoinCode,
        @RequestParam String bitcoinName,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return bitcoinServiceApiV1.getBitcoinDetail(bitcoinCode, bitcoinName, customUserDetails);
    }
}
