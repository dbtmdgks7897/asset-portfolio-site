package com.ysh.back.domain.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.admin.dto.ReqAdminUserDeletedDataDTO;
import com.ysh.back.domain.admin.dto.ReqAdminUserSuspendData;
import com.ysh.back.domain.admin.service.AdminServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "관리 페이지", description = "유저 및 게시물 관리 페이지")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminControllerApiV1 {

    @Autowired
    AdminServiceApiV1 adminServiceApiV1;

    // TODO : 좋은 방법 있는지 물어보기
    @Operation(summary = "유저 리스트 / 검색",
    description = "검색어 있으면 검색, 없으면 전체 리스트")
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

    // TODO : 더 좋은 방법 물어보기
    @Operation(summary = "유저 정지(비활성화) / 활성화",
    description = "정지 기간 / 정지 이유 적고 정지 맥이기, 활성화 <br />"
    + "req의 suspendDuration이 null이면 비활성화로 인식")
    @PutMapping("/user/{userIdx}/suspend")
    public ResponseEntity<?> putUserSuspendData(
        @PathVariable Long userIdx,
        @Valid @RequestBody(required = false) ReqAdminUserSuspendData reqAdminUserSuspendData,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        if(reqAdminUserSuspendData.getSuspendDuration() != null){
            return adminServiceApiV1.insertUserSuspendData(userIdx, reqAdminUserSuspendData, customUserDetails);
        }else{
            return adminServiceApiV1.updateUserDisSuspendData(userIdx, customUserDetails);
        }
    }

    @Operation(summary = "유저 리스트 / 검색",
    description = "검색어 있으면 검색, 없으면 전체 리스트")
    @PostMapping("/user/{userIdx}/deletedAt")
    public ResponseEntity<?> putUserDeleteData(
        @PathVariable Long userIdx,
        @Valid @RequestBody(required = false) ReqAdminUserDeletedDataDTO reqAdminUserDeletedDataDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        if(reqAdminUserDeletedDataDTO != null){
            return adminServiceApiV1.insertUserDeleteData(userIdx, reqAdminUserDeletedDataDTO, customUserDetails);
        } else {
            return adminServiceApiV1.updateUserRestoreData(userIdx, customUserDetails);
        }
    }
    
}
