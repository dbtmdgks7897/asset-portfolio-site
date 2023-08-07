package com.ysh.back.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.auth.dto.ReqJoinDTO;
import com.ysh.back.domain.auth.service.AuthServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

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
}
