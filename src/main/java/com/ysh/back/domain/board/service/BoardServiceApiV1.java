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
import com.ysh.back.domain.board.dto.ResBoardDetailsDTO;
import com.ysh.back.domain.board.dto.ResBoardListDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;

@Service
public class BoardServiceApiV1 {
    
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    AuditLogRepository auditLogRepository;

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
        
        if(boardEntityList.isEmpty()){
            throw new BadRequestException("게시물 조회 오류");
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
            throw new BadRequestException("게시물 상세 조회 오류");
        }

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 상세 조회 성공")
            .data(ResBoardDetailsDTO.of(boardEntityOptional.get()))
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

}
