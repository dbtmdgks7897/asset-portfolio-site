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
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.board.dto.ReqBoardPostDTO;
import com.ysh.back.domain.board.dto.ReqBoardReportDTO;
import com.ysh.back.domain.board.dto.ReqBoardUpdateDTO;
import com.ysh.back.domain.board.dto.ResBoardDetailsDTO;
import com.ysh.back.domain.board.dto.ResBoardListDTO;
import com.ysh.back.domain.board.dto.ResBoardUpdateInitDTO;
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
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

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
    @Autowired
    UserRepository userRepository;

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
            boardEntityList = boardRepository.findByNameContainingOrContentContainingOrUserEntity_NameContainingAndDeletedAtIsNull(search, search, search);
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

    @Transactional
    public ResponseEntity<?> postBoardData(CustomUserDetails customUserDetails, ReqBoardPostDTO reqBoardPostDTO){
        
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보가 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        BoardEntity boardEntity = BoardEntity.builder()
        .userEntity(userEntity)
        .name(reqBoardPostDTO.getName())
        .content(reqBoardPostDTO.getContent())
        .build();

        boardRepository.save(boardEntity);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board")
        // 유저 정보 가져올 수 있을 때 바꾸자.
        .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
        .rowId(boardRepository.count())
        .operation("INSERT")
        .reason("게시물 작성")
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물이 저장되었습니다.")
            .data(boardRepository.count())
            .build(),
            HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<?> getBoardDetailsData(Long boardIdx) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardIdx);
        
        if(boardEntityOptional.isEmpty()){
            throw new BadRequestException("존재하지 않는 게시물입니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setViewCount(boardEntity.getViewCount()+1);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 상세 조회 성공")
            .data(ResBoardDetailsDTO.fromEntity(boardEntity))
            .build()   
        ,HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardCommentListData(Long boardIdx) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntityIdxAndDeletedAtIsNullOrderByCreatedAtDesc(boardIdx);
        
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
        
        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 삭제 성공")
            .build()   
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertBoardReportData(Long boardIdx, ReqBoardReportDTO reqBoardReportDTO, CustomUserDetails customUserDetails){
        Optional<BoardReportEntity> boardReportEntityOptional = boardReportRepository.findByBoardIdxAndUserIdx(boardIdx, userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx());

        if(boardReportEntityOptional.isPresent()){
            throw new BadRequestException("이미 신고한 게시물입니다");
        }

        Long userIdx = userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx(); 

        BoardReportEntity boardReportEntityForSaving = BoardReportEntity.builder()
        .userIdx(userIdx)
        .boardIdx(boardIdx)
        .reason(reqBoardReportDTO.getReason())
        .build();

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board-report")
        .userIdx(userIdx)
        .rowId(boardReportRepository.count())
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
    public ResponseEntity<?> insertBoardRecommendData(Long boardIdx, CustomUserDetails customUserDetails){
        Optional<BoardRecommendEntity> boardRecommendEntityOptional = boardRecommendRepository.findByBoardIdxAndUserIdx(boardIdx, userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx());

        if(boardRecommendEntityOptional.isPresent()){
            throw new BadRequestException("이미 추천한 게시물입니다");
        }

        Long userIdx = userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx();

        BoardRecommendEntity boardRecommendEntityForSaving = BoardRecommendEntity.builder()
        .userIdx(userIdx)
        .boardIdx(boardIdx)
        .build();

        boardRecommendRepository.save(boardRecommendEntityForSaving);        

        boardRepository.incrementRecommendCount(boardIdx);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board-recommend")
        .userIdx(userIdx)
        .rowId(boardRecommendRepository.count())
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

    public ResponseEntity<?> getBoardUpdateInitData(Long boardIdx){
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardIdx);

        if(boardEntityOptional.isEmpty()){
            throw new BadRequestException("게시물?이 없습니다?");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        ResBoardUpdateInitDTO resBoardUpdateInitDTO = ResBoardUpdateInitDTO.builder()
        .name(boardEntity.getName())
        .content(boardEntity.getContent())
        .build();

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 정보 들고옴")
            .data(resBoardUpdateInitDTO)
            .build()   
        ,HttpStatus.OK);

    }

    @Transactional
     public ResponseEntity<?> updateBoardData(Long boardIdx, ReqBoardUpdateDTO dto){
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if(boardEntityOptional.isEmpty()){
            throw new BadRequestException("해당 게시물이 존재하지 않습니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setName(dto.getName());
        boardEntity.setContent(dto.getContent());
        boardEntity.setUpdatedAt(dto.getUpdatedAt());

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("board")
        .userIdx(boardEntity.getUserEntity().getIdx())
        .rowId(boardIdx)
        .operation("UPDATE")
        .reason("게시물 수정")
        .oldValue("게시물 제목 : " + dto.getNameOrigin()
        + "\n게시물 내용 : " + dto.getContentOrigin())
        .newValue("게시물 제목 : " + dto.getName()
        + "\n게시물 내용 : " + dto.getContent())
        .build();
        auditLogRepository.save(auditLog);


        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("게시물 수정 성공")
            .build()   
        ,HttpStatus.OK);
    }
}
