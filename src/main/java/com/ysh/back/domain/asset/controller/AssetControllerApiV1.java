package com.ysh.back.domain.asset.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Asset(서버 내 전체, 내 자산 아님)", description = "")
@RestController
@RequestMapping("/api/v1/asset")
public class AssetControllerApiV1 {

    @Autowired
    AssetServiceApiV1 assetServiceApiV1;

    @Operation(summary = "Asset 등록", description = "서버에 자산 정보 등록")
    @PostMapping()
    public ResponseEntity<?> postAssetData(
        @RequestBody ReqPostAssetDTO reqPostAssetDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return assetServiceApiV1.postAssetData(reqPostAssetDTO, customUserDetails);
    }
}
