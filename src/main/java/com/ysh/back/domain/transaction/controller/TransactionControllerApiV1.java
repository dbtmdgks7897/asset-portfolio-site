package com.ysh.back.domain.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.transaction.service.TransactionServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "거래 내역", description = "거래 내역 조회")
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionControllerApiV1 {

    @Autowired
    TransactionServiceApiV1 transactionServiceApiV1;


    
    @Operation(summary = "거래 내역 전체 조회", description = "")
    @GetMapping()
    public ResponseEntity<?> getAllTransactionList(
        @RequestParam Integer portfolioIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return transactionServiceApiV1.getAllTransactionList(portfolioIdx, customUserDetails);
    }
}
