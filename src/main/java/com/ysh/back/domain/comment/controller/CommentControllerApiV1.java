package com.ysh.back.domain.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.comment.dto.ReqCommentPostDTO;
import com.ysh.back.domain.comment.dto.ReqCommentRecommendDTO;
import com.ysh.back.domain.comment.dto.ReqCommentReportDTO;
import com.ysh.back.domain.comment.service.CommentServiceApiV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "댓글", description = "댓글 관련")
@RequestMapping("/api/v1/comment")
public class CommentControllerApiV1{
    
    @Autowired
    CommentServiceApiV1 commentServiceApiV1;


    @Operation(summary = "댓글 작성",
    description = "작성한다. 댓글")
    @PostMapping("/{boardIdx}")
    public ResponseEntity<?> postCommentData(
        @PathVariable Long boardIdx,
        @Valid @RequestBody ReqCommentPostDTO reqCommentPostDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return commentServiceApiV1.postCommentData(boardIdx, reqCommentPostDTO, customUserDetails);
    }


    @Operation(summary = "댓글 신고",
    description = "신고 버튼 클릭 시 현재 유저가 누른 적 있는 지 판별<br />"
    + "없으면 신고 정보 comment_report 테이블 등록")
    @PostMapping("/{commentIdx}/report")
    public ResponseEntity<?> insertCommentReportData(
        @PathVariable Long commentIdx,
        @Valid @RequestBody ReqCommentReportDTO reqCommentReportDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentServiceApiV1.insertCommentReportData(commentIdx, reqCommentReportDTO, customUserDetails);
    }

    @Operation(summary = "댓글 추천",
    description = "추천 버튼 클릭 시 현재 유저가 누른 적 있는 지 판별<br />"
    + "없으면 추천 정보 comment_recommend 테이블 등록")
    @PostMapping("/{commentIdx}/recommend")
    public ResponseEntity<?> insertCommentRecommendData(
        @PathVariable Long commentIdx,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commentServiceApiV1.insertCommentRecommendData(commentIdx, customUserDetails);
    }
}
