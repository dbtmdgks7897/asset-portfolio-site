package com.ysh.back.domain.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.domain.board.dto.ReqBoardRecommendDTO;
import com.ysh.back.domain.board.dto.ReqBoardReportDTO;
import com.ysh.back.domain.board.dto.ResBoardDetailsDTO;
import com.ysh.back.domain.board.dto.ResBoardListDTO;
import com.ysh.back.domain.comment.dto.ResBoardCommentListDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.entity.BoardRecommendEntity;
import com.ysh.back.model.board.entity.BoardReportEntity;
import com.ysh.back.model.board.repository.BoardRecommendRepository;
import com.ysh.back.model.board.repository.BoardReportRepository;
import com.ysh.back.model.board.repository.BoardRepository;
import com.ysh.back.model.comment.entity.CommentEntity;
import com.ysh.back.model.comment.repository.CommentRepository;

@Service
public class BoardServiceApiV1 {
    
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BoardReportRepository boardReportRepository;
    @Autowired
    BoardRecommendRepository boardRecommendRepository;

    // 게시물 리스트 뿌려주기 검색어, 정렬기준 입력 가능
    public ResponseEntity<?> getBoardListData(String search, String sort, Boolean desc){
        List<BoardEntity> boardEntityList;
        if((search != null) && (sort != null)){
            // 검색어, 정렬 기준 둘 다 입력
            if(desc.equals(true)){
                boardEntityList = boardRepository.findAllSearchedAndOrderBySortDesc(search, search, search, sort);
            } else {
                boardEntityList = boardRepository.findAllSearchedAndOrderBySort(search, search, search, sort);
            }
            
        } else if (search != null){
            // 검색어만 입력시
            boardEntityList = boardRepository.findByNameContainingOrContentContainingOrUserEntity_NameContaining(search, search, search);
        } else if (sort != null){
            // 정렬 기준만 입력 시
            if(desc.equals(true)){
                boardEntityList = boardRepository.findAllOrderBySortDesc(sort);
            }else{
                boardEntityList = boardRepository.findAllOrderBySort(sort);
            }
        } else {
            // 검색어, 정렬 기준 없음
            boardEntityList = boardRepository.findAll();
        }
        
        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 조회 성공")
            .data(ResBoardListDTO.of(boardEntityList))
            .build()   
        ,HttpStatus.OK);

    }

    public ResponseEntity<?> getBoardDetailsData(Long boardIdx) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardIdx);
        
        if(boardEntityOptional.isEmpty()){
            throw new BadRequestException("존재하지 않는 게시물입니다.");
        }

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 상세 조회 성공")
            .data(ResBoardDetailsDTO.fromEntity(boardEntityOptional.get()))
            .build()   
        ,HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardCommentListData(Long boardIdx) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntityIdxOrderByCreatedAtAsc(boardIdx);
        
        if(commentEntityList.isEmpty()){
            return null;
        }

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 댓글 조회 성공")
            .data(ResBoardCommentListDTO.of(commentEntityList))
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteBoardData(Long boardIdx) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardIdx);
        
        if(boardEntityOptional.isEmpty()){
            throw new BadRequestException("존재하지 않는 게시물입니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        // TODO : 작성자와 현재 유저 매치 기능 추가 필요


        boardEntity.setDeletedAt(LocalDateTime.now());


        // TODO : 나중에 자동화 하는 방법 있나 물어보기
        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board")
        // 유저 정보 가져올 수 있을 때 바꾸자.
        .userIdx(boardEntity.getUserEntity().getIdx())
        .rowId(boardEntity.getIdx())
        .operation("DELETE")
        .reason("게시자 삭제")
        .build();
        
        
        boardRepository.delete(boardEntity);
        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 삭제 성공")
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertBoardReportData(Long boardIdx, ReqBoardReportDTO reqBoardReportDTO){
        Optional<BoardReportEntity> boardReportEntityOptional = boardReportRepository.findByBoardIdxAndUserIdx(boardIdx, (Long)reqBoardReportDTO.getUserIdx());

        if(boardReportEntityOptional.isPresent()){
            throw new BadRequestException("이미 신고한 게시물입니다");
        }

        BoardReportEntity boardReportEntityForSaving = BoardReportEntity.builder()
        .userIdx(reqBoardReportDTO.getUserIdx())
        .boardIdx(boardIdx)
        .reason(reqBoardReportDTO.getReason())
        .build();

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board-report")
        .userIdx(reqBoardReportDTO.getUserIdx())
        .operation("INSERT")
        .reason("게시물 신고")
        .newValue("신고 게시물 : " + boardIdx)
        .build();

        boardReportRepository.save(boardReportEntityForSaving);
        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 신고 성공")
            .build()   
        ,HttpStatus.OK);
    }
    
    @Transactional
    public ResponseEntity<?> insertBoardRecommendData(Long boardIdx, ReqBoardRecommendDTO reqBoardRecommendDTO){
        Optional<BoardRecommendEntity> boardRecommendEntityOptional = boardRecommendRepository.findByBoardIdxAndUserIdx(boardIdx, (Long)reqBoardRecommendDTO.getUserIdx());

        if(boardRecommendEntityOptional.isPresent()){
            throw new BadRequestException("이미 추천한 게시물입니다");
        }

        BoardRecommendEntity boardRecommendEntityForSaving = BoardRecommendEntity.builder()
        .userIdx(reqBoardRecommendDTO.getUserIdx())
        .boardIdx(boardIdx)
        .build();

        boardRecommendRepository.save(boardRecommendEntityForSaving);        

        boardRepository.incrementRecommendCount(boardIdx);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board-recommend")
        .userIdx(reqBoardRecommendDTO.getUserIdx())
        .operation("INSERT")
        .reason("게시물 추천")
        .newValue("추천 게시물 :" + boardIdx)
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 추천 성공")
            .build()   
        ,HttpStatus.OK);
    }
}
