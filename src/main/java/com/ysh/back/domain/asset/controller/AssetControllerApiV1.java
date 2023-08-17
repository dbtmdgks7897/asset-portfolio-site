package com.ysh.back.domain.asset.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.dto.ReqAssetPurchaseDTO;
import com.ysh.back.domain.asset.dto.ReqAssetSellDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Asset(서버 내 전체, 내 자산 아님)", description = "")
@RestController
@RequestMapping("/api/v1/asset")
public class AssetControllerApiV1 {

    @Autowired
    AssetServiceApiV1 assetServiceApiV1;

    @Operation(summary = "자산 등록", description = "서버에 자산 정보 등록")
    @PostMapping()
    public ResponseEntity<?> postAssetData(
        @RequestBody ReqPostAssetDTO reqPostAssetDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return assetServiceApiV1.postAssetData(reqPostAssetDTO, customUserDetails);
    }

    @Operation(summary = "자산 구매", description = "자산 구매 컨트롤러")
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseAsset(
        @Valid @RequestBody ReqAssetPurchaseDTO reqAssetPurchaseDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return assetServiceApiV1.purchaseAsset(reqAssetPurchaseDTO, customUserDetails);
    }

    @Operation(summary = "자산 판매", description = "자산 판매 컨트롤러")
    @PostMapping("/sell")
    public ResponseEntity<?> sellAsset(
        @Valid @RequestBody ReqAssetSellDTO reqAssetSellDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return assetServiceApiV1.sellAsset(reqAssetSellDTO, customUserDetails);
    }

    @Operation(summary = "자산 목록 조회", description = "서버에 있는 자산 정보 조회")
    @GetMapping()
    public ResponseEntity<?> getAssetListData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return assetServiceApiV1.getAssetListData(customUserDetails);
    }
}
