package com.ysh.back.domain.comment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.domain.comment.dto.ReqCommentRecommendDTO;
import com.ysh.back.domain.comment.dto.ReqCommentReportDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.comment.entity.CommentRecommendEntity;
import com.ysh.back.model.comment.entity.CommentReportEntity;
import com.ysh.back.model.comment.repository.CommentRecommendRepository;
import com.ysh.back.model.comment.repository.CommentReportRepository;
import com.ysh.back.model.comment.repository.CommentRepository;

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

    @Transactional
    public ResponseEntity<?> insertCommentReportData(Long commentIdx, ReqCommentReportDTO reqCommentReportDTO) {
        Optional<CommentReportEntity> commnetReportEntityOptional = commentReportRepository
                .findByCommentIdxAndUserIdx(commentIdx, reqCommentReportDTO.getUserIdx());

        if (commnetReportEntityOptional.isPresent()) {
            throw new BadRequestException("이미 신고한 댓글입니다.");
        }

        CommentReportEntity commentReportEntityForSaving = CommentReportEntity.builder()
                .commentIdx(commentIdx)
                .userIdx(reqCommentReportDTO.getUserIdx())
                .reason(reqCommentReportDTO.getReason())
                .build();
        
        commentReportRepository.save(commentReportEntityForSaving);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment-report")
        .userIdx(reqCommentReportDTO.getUserIdx())
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
    public ResponseEntity<?> insertCommentRecommendData(Long commentIdx, ReqCommentRecommendDTO reqCommentRecommendDTO) {
        Optional<CommentRecommendEntity> commnetRecommendEntityOptional = commentRecommendRepository.findByCommentIdxAndUserIdx(commentIdx, reqCommentRecommendDTO.getUserIdx());

        if (commnetRecommendEntityOptional.isPresent()) {
            throw new BadRequestException("이미 추천한 댓글입니다.");
        }

        CommentRecommendEntity commentRecommendEntityForSaving = CommentRecommendEntity.builder()
                .commentIdx(commentIdx)
                .userIdx(reqCommentRecommendDTO.getUserIdx())
                .build();
        
        commentRecommendRepository.save(commentRecommendEntityForSaving);
        commentRepository.incrementRecommendCount(commentIdx);


        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment-report")
        .userIdx(reqCommentRecommendDTO.getUserIdx())
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
