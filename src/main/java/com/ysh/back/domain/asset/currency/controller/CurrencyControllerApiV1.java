package com.ysh.back.domain.asset.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.currency.dto.ReqCurrencyPurchaseDTO;
import com.ysh.back.domain.asset.currency.service.CurrencyServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "외화", description = "외화 도메인")
@RestController
@RequestMapping("/api/v1/asset/currency")
public class CurrencyControllerApiV1 {

    @Autowired
    CurrencyServiceApiV1 currencyServiceApiV1;


    @Operation(summary = "외화 구매", description = "외화 구매 컨트롤러")
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseCurrency(
        @Valid @RequestBody ReqCurrencyPurchaseDTO reqCurrencyPurchaseDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return currencyServiceApiV1.purchaseCurrency(reqCurrencyPurchaseDTO, customUserDetails);
    }

    // @Operation(summary = "외화 판매", description = "외화 판매 컨트롤러")
    // @PostMapping("/sell")
    // public ResponseEntity<?> sellCurrency() {

    // }
}
