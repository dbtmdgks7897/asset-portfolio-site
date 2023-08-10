package com.ysh.back.domain.mypage.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
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
    @GetMapping("/info")
    public ResponseEntity<?> postMyinfoInitData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return mypageServiceApiV1.getMyinfoInitData(customUserDetails);
    }

    // TODO : 이미지 업로드
    @Operation(summary = "내 정보 수정",
    description = "수정 버튼 클릭 시 정보 수정 창 진입")
    // @RequestMapping(method = RequestMethod.POST, path = "/infoUp", consumes = "multipart/form-data")
    @PostMapping("/info/update")
    public ResponseEntity<?> updateMyinfoData(
        // @ModelAttribute("formData") ReqMyinfoUpdateDTO reqMyinfoUpdateDTO) throws IOException
        @Valid @RequestBody ReqMyinfoUpdateDTO reqMyinfoUpdateDTO
        )
 {
        System.out.println("이거다" + reqMyinfoUpdateDTO.getIdx());
        return mypageServiceApiV1.updateMyinfoData(reqMyinfoUpdateDTO);
    }

    @Operation(summary = "회원 탈퇴",
    description = "회원 탈퇴 기능. 삭제하지 않고 deletedAt에 현재 날짜 저장")
    @DeleteMapping("/info")
    public ResponseEntity<?> deleteMyinfoData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return mypageServiceApiV1.deleteMyinfoData(customUserDetails);
    }

    @Operation(summary = "내 활동",
    description = "내 게시물 / 댓글 가져오기")
    @GetMapping("/active")
    public ResponseEntity<?> getMyactiveData(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        System.out.println(customUserDetails.getUsername());
        return mypageServiceApiV1.getMyactiveData(customUserDetails);
    }
    
}
