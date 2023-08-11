package com.ysh.back.domain.comment.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.comment.dto.ReqCommentPostDTO;
import com.ysh.back.domain.comment.dto.ReqCommentReportDTO;
import com.ysh.back.domain.comment.dto.ReqCommentUpdateDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;
import com.ysh.back.model.comment.entity.CommentEntity;
import com.ysh.back.model.comment.entity.CommentRecommendEntity;
import com.ysh.back.model.comment.entity.CommentReportEntity;
import com.ysh.back.model.comment.repository.CommentRecommendRepository;
import com.ysh.back.model.comment.repository.CommentReportRepository;
import com.ysh.back.model.comment.repository.CommentRepository;
import com.ysh.back.model.user.entity.UserEntity;
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
    @Autowired
    BoardRepository boardRepository;

    @Transactional
    public ResponseEntity<?> postCommentData(Long boardIdx, ReqCommentPostDTO reqCommentPostDTO, CustomUserDetails customUserDetails) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if(!boardEntityOptional.isPresent()){
            throw new BadRequestException("해당 게시물이 존재하지 않습니다.");
        }

        UserEntity userEntity = userRepository.findByEmail(customUserDetails.getUsername()).get();

        CommentEntity commentEntity = CommentEntity.builder()
        .userEntity(userEntity)
        .boardEntity(boardEntityOptional.get())
        .content(reqCommentPostDTO.getContent())
        .build();

        commentRepository.save(commentEntity);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment")
        .userIdx(userEntity.getIdx())
        .rowId(commentRepository.count())
        .operation("INSERT")
        .reason("댓글 작성")
        .newValue(reqCommentPostDTO.getContent())
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("댓글 작성 완료")
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateCommentData(Long commentIdx, ReqCommentUpdateDTO reqCommentUpdateDTO, CustomUserDetails customUserDetails) {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findByIdx(commentIdx);

        if(!commentEntityOptional.isPresent()){
            throw new BadRequestException("해당 댓글 정보가 없습니다.");
        }
        CommentEntity commentEntity = commentEntityOptional.get();

        if(commentEntity.getUserEntity().getIdx() != userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx()){
            throw new BadRequestException("다른 사용자의 댓글입니다.");
        }
        String oldVal = commentEntity.getContent();
        commentEntity.setContent(reqCommentUpdateDTO.getContent());

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment")
        .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
        .rowId(commentIdx)
        .operation("UPDATE")
        .reason("댓글 수정")
        .oldValue(oldVal)
        .newValue(reqCommentUpdateDTO.getContent())
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("댓글 수정 완료")
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteCommentData(Long commentIdx, CustomUserDetails customUserDetails) {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findByIdx(commentIdx);

        if(!commentEntityOptional.isPresent()){
            throw new BadRequestException("해당 댓글 정보가 없습니다.");
        }

        CommentEntity commentEntity = commentEntityOptional.get();

        if(commentEntity.getUserEntity().getIdx() != userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx()){
            throw new BadRequestException("다른 사용자의 댓글입니다.");
        }

        String oldVal = commentEntity.getContent();
        commentEntity.setDeletedAt(LocalDateTime.now());

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("comment")
        .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
        .rowId(commentIdx)
        .operation("DELETE")
        .reason("댓글 삭제")
        .oldValue(oldVal)
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("댓글 삭제 완료")
            .build()   
        ,HttpStatus.OK);
    }

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
