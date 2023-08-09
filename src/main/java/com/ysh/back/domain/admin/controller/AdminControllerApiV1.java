package com.ysh.back.domain.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.admin.service.AdminServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "관리 페이지", description = "유저 및 게시물 관리 페이지")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminControllerApiV1 {

    @Autowired
    AdminServiceApiV1 adminServiceApiV1;
    
    @Operation(summary = "관리자 페이지 - 유저",
    description = "유저들 상세 정보, 비활성화 / 활성화 / 탈퇴 조작")
    @GetMapping("/user")
    public ResponseEntity<?> getAdminUserData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return adminServiceApiV1.getAdminUserData(customUserDetails);
    }
}
