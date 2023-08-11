package com.ysh.back.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.auth.dto.ReqJoinDTO;
import com.ysh.back.domain.auth.service.AuthServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "인증 / 인가", description = "사용자 정보 인가 / 인증 관련")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerApiV1 {

    @Autowired
    AuthServiceApiV1 authServiceApiV1;
    
    @Operation(summary = "회원가입",
    description = "회원가입 창에서 정보를 받아 DB에 유저 정보 저장")
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody ReqJoinDTO dto) {
        return authServiceApiV1.join(dto);
    }

    @Operation(summary = "내 정보 가져오기",
    description = "CustomUserDetails로 내 정보를 받아와 필수 정보 반환")
    @GetMapping("/my")
    public ResponseEntity<?> getMy(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return authServiceApiV1.getMy(customUserDetails);
    }
}
