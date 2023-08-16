package com.ysh.back.domain.asset.stock.domestic.bookmark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.stock.domestic.bookmark.dto.ReqPostBookmarkDTO;
import com.ysh.back.domain.asset.stock.domestic.bookmark.service.BookmarkServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "즐겨찾기", description = "즐겨찾기인데?")
@RestController
@RequestMapping("/api/v1/bookmark")
public class BookmarkControllerApiV1 {

    @Autowired
    BookmarkServiceApiV1 bookmarkServiceApiV1;

    @Operation(summary = "즐겨찾기 등록 여부", description = "즐겨찾기 되어있으면 true, 아니면 false")
    @GetMapping()
    public boolean isBookmarked(
        @RequestParam String assetCode,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return bookmarkServiceApiV1.isBookmarked(assetCode, customUserDetails);
    }

    @Operation(summary = "즐겨찾기 등록 / 삭제", description = "즐겨찾기 등록 / 되어있으면 삭제")
    @PostMapping()
    public ResponseEntity<?> postBookmarkData(
        @Valid @RequestBody ReqPostBookmarkDTO reqPostBookmarkDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return bookmarkServiceApiV1.postBookmarkData(reqPostBookmarkDTO, customUserDetails);
    }
}
