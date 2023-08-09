package com.ysh.back.domain.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.admin.service.AdminServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "관리 페이지", description = "유저 및 게시물 관리 페이지")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminControllerApiV1 {

    @Autowired
    AdminServiceApiV1 adminServiceApiV1;
    // TODO : 좋은 방법 있는지 물어보기
    @Operation(summary = "유저 관리 페이지 / 유저 검색",
    description = "유저들 상세 정보, 비활성화 / 활성화 / 탈퇴 조작 페이지")
    @GetMapping("/user")
    public ResponseEntity<?> getAdminUserData(
        @RequestParam(required = false) String search
    ){
        if(search == null){
            return adminServiceApiV1.getAdminUserData();
        } else {
            return adminServiceApiV1.getAdminUserSearchData(search);
        }
        
    }
}
