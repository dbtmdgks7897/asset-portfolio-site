package com.ysh.back.domain.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.service.AssetTypeServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "자산 타입", description = "")
@RestController
@RequestMapping("/api/v1/asset/type")
public class AssetTypeControllerApiV1 {
    
    @Autowired
    AssetTypeServiceApiV1 assetTypeServiceApiV1;

    @Operation(summary = "자산 타입 리스트", description = "전체 자산 타입 리스트 받아오기")
    @GetMapping()
    public ResponseEntity<?> getAssetTypeList(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return assetTypeServiceApiV1.getAssetTypeList(customUserDetails);
    }

}
