package com.ysh.back.domain.comment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.comment.dto.ReqCommentRecommendDTO;
import com.ysh.back.domain.comment.dto.ReqCommentReportDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.comment.entity.CommentRecommendEntity;
import com.ysh.back.model.comment.entity.CommentReportEntity;
import com.ysh.back.model.comment.repository.CommentRecommendRepository;
import com.ysh.back.model.comment.repository.CommentReportRepository;
import com.ysh.back.model.comment.repository.CommentRepository;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceApiV1 {

    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentReportRepository commentReportRepository;
    @Autowired
    CommentRecommendRepository commentRecommendRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> insertCommentReportData(Long commentIdx, ReqCommentReportDTO reqCommentReportDTO, CustomUserDetails customUserDetails) {
        Optional<CommentReportEntity> commnetReportEntityOptional = commentReportRepository
                .findByCommentIdxAndUserIdx(commentIdx, userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx());

        if (commnetReportEntityOptional.isPresent()) {
            throw new BadRequestException("이미 신고한 댓글입니다.");
        }

        Long userIdx = userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx();

        CommentReportEntity commentReportEntityForSaving = CommentReportEntity.builder()
                .commentIdx(commentIdx)
                .userIdx(userIdx)
                .reason(reqCommentReportDTO.getReason())
                .build();
        
        commentReportRepository.save(commentReportEntityForSaving);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment-report")
        .userIdx(userIdx)
        .rowId(commentReportRepository.count())
        .operation("INSERT")
        .reason("댓글 신고")
        .newValue("신고 댓글 : " + commentIdx)
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("댓글 신고 성공")
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertCommentRecommendData(Long commentIdx, CustomUserDetails customUserDetails) {
        Optional<CommentRecommendEntity> commnetRecommendEntityOptional = commentRecommendRepository.findByCommentIdxAndUserIdx(commentIdx,userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx());

        if (commnetRecommendEntityOptional.isPresent()) {
            throw new BadRequestException("이미 추천한 댓글입니다.");
        }

        Long userIdx = userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx();

        CommentRecommendEntity commentRecommendEntityForSaving = CommentRecommendEntity.builder()
                .commentIdx(commentIdx)
                .userIdx(userIdx)
                .build();
        
        commentRecommendRepository.save(commentRecommendEntityForSaving);
        commentRepository.incrementRecommendCount(commentIdx);


        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment-report")
        .userIdx(userIdx)
        .rowId(commentRecommendRepository.count())
        .operation("INSERT")
        .reason("댓글 신고")
        .newValue("신고 댓글 : " + commentIdx)
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("댓글 신고 성공")
            .build()   
        ,HttpStatus.OK);
    }
}
