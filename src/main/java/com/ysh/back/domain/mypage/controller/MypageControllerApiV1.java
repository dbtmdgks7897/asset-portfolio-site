package com.ysh.back.domain.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.domain.mypage.dto.ReqMyinfoInitDTO;
import com.ysh.back.domain.mypage.dto.ReqMyinfoUpdateDTO;
import com.ysh.back.domain.mypage.service.MypageServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "내 계정(정보)", description = "내 정보 / 내 활동")
@RestController
@RequestMapping("/api/v1/mypage")
public class MypageControllerApiV1 {

    @Autowired
    MypageServiceApiV1 mypageServiceApiV1;

    @Operation(summary = "내 정보 조회",
    description = "내 정보인데?")
    @PostMapping("/info")
    public ResponseEntity<?> postMyinfoInitData(
        @Valid @RequestBody ReqMyinfoInitDTO reqMyinfoInitDTO){
        return mypageServiceApiV1.getMyinfoInitData(reqMyinfoInitDTO);
    }

    @Operation(summary = "내 정보 수정",
    description = "수정 버튼 클릭 시 정보 수정 창 진입")
    @RequestMapping(method = RequestMethod.POST, path = "/infoUp", consumes = "multipart/form-data")
    public ResponseEntity<?> updateMyinfoData(
        @Valid @RequestBody ReqMyinfoUpdateDTO reqMyinfoUpdateDTO){
        System.out.println("이거다" + reqMyinfoUpdateDTO.getIdx());
        return mypageServiceApiV1.updateMyinfoData(reqMyinfoUpdateDTO);
    }

    @Operation(summary = "회원 탈퇴",
    description = "회원 탈퇴 기능. 삭제하지 않고 deletedAt에 현재 날짜 저장")
    @DeleteMapping("/info")
    public ResponseEntity<?> deleteMyinfoData(
        @RequestParam Long userIdx){
        return mypageServiceApiV1.deleteMyinfoData(userIdx);
    }
}
