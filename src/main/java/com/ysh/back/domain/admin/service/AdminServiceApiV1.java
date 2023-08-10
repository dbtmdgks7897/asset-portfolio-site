package com.ysh.back.domain.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.admin.dto.ReqAdminBoardDeletedDataDTO;
import com.ysh.back.domain.admin.dto.ReqAdminBoardHideData;
import com.ysh.back.domain.admin.dto.ReqAdminUserDeletedDataDTO;
import com.ysh.back.domain.admin.dto.ReqAdminUserSuspendData;
import com.ysh.back.domain.admin.dto.ResAdminBoardInitDTO;
import com.ysh.back.domain.admin.dto.ResAdminUserInitDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    BoardRepository boardRepository;

    // TODO : board랑 비교해서 뭐가 나은지 (검색)
    public ResponseEntity<?> getAdminUserData() {
        List<UserEntity> userEntityList = userRepository.findAll();

        ResAdminUserInitDTO resAdminUserInitDTO = ResAdminUserInitDTO.of(userEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 리스트 조회 성공")
                        .data(resAdminUserInitDTO)
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> getAdminUserSearchData(String search) {
        List<UserEntity> userEntityList;
        Long searchLong = -1L;
        try {
            searchLong = Long.parseLong(search);
        } catch (Exception e) {

        } finally {
            userEntityList = userRepository.findByIdxOrEmailContainingOrNicknameContaining(searchLong, search, search);
        }

        if (userEntityList.isEmpty()) {
            throw new BadRequestException("검색 결과 없음");
        }

        ResAdminUserInitDTO resAdminUserInitDTO = ResAdminUserInitDTO.of(userEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 검색 성공")
                        .data(resAdminUserInitDTO)
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertUserSuspendData(Long userIdx, ReqAdminUserSuspendData reqAdminUserSuspendData,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(userIdx);

        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("유저 정보가 없습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();

        userEntity.setSuspendUntil(LocalDateTime.now().plusDays(reqAdminUserSuspendData.getSuspendDuration()));
        userEntity.setSuspendReason(reqAdminUserSuspendData.getSuspendReason());

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("suspend_until")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("INSERT")
                .newValue(reqAdminUserSuspendData.getSuspendDuration().toString())
                .reason(reqAdminUserSuspendData.getSuspendReason())
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 정지 처리 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateUserDisSuspendData(Long userIdx, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(userIdx);

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("유저 정보가 없습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();

        userEntity.setSuspendUntil(null);
        String oldVal = userEntity.getSuspendReason();
        userEntity.setSuspendReason(null);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("suspend_until")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("UPDATE")
                .oldValue(oldVal)
                .newValue(null)
                .reason(null)
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 정지 해제 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertUserDeleteData(Long userIdx, ReqAdminUserDeletedDataDTO reqAdminUserDeletedDataDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(userIdx);

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("유저 정보가 없습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();


        userEntity.setDeletedAt(LocalDateTime.now());
        userEntity.setDeletedReason(reqAdminUserDeletedDataDTO.getReason());

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("deleted_at")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("DELETE")
                .newValue(LocalDateTime.now().toString())
                .reason(reqAdminUserDeletedDataDTO.getReason())
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 탈퇴 처리 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateUserRestoreData(Long userIdx, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(userIdx);

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("유저 정보가 없습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();


        userEntity.setDeletedAt(null);
        String oldVal = userEntity.getDeletedReason();
        userEntity.setDeletedReason(null);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("deleted_at")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("UPDATE")
                .oldValue(oldVal)
                .newValue(null)
                .reason(null)
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 탈퇴 복구 완료")
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> getAdminBoardData() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        ResAdminBoardInitDTO resAdminBoardInitDTO = ResAdminBoardInitDTO.of(boardEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("유저 리스트 조회 성공")
                        .data(resAdminBoardInitDTO)
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> getAdminBoardSearchData(String search) {
        List<BoardEntity> boardEntityList;
        Long searchLong = -1L;
        try {
            searchLong = Long.parseLong(search);
        } catch (Exception e) {

        } finally {
            boardEntityList = boardRepository.findByIdxOrNameContainingOrUserEntity_EmailContaining(searchLong, search, search);
        }

        if (boardEntityList.isEmpty()) {
            throw new BadRequestException("검색 결과 없음");
        }

        ResAdminBoardInitDTO resAdminBoardInitDTO = ResAdminBoardInitDTO.of(boardEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("게시물 검색 성공")
                        .data(resAdminBoardInitDTO)
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertBoardHideData(Long boardIdx, ReqAdminBoardHideData reqAdminBoardHideData,
            CustomUserDetails customUserDetails) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if (!boardEntityOptional.isPresent()) {
            throw new BadRequestException("게시물 정보가 없습니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setIsHided(true);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("board")
                .columnName("isHide")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(boardIdx)
                .operation("INSERT")
                .newValue("true")
                .reason(reqAdminBoardHideData.getHideReason())
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("게시물 숨김 처리 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateBoardHideData(Long boardIdx, CustomUserDetails customUserDetails){
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if(!boardEntityOptional.isPresent()){
            throw new BadRequestException("게시물 정보가 없습니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setIsHided(false);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("isHide")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(boardIdx)
                .operation("UPDATE")
                .newValue(null)
                .reason(null)
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("게시물 숨김 해제 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> insertBoardDeletedData(Long boardIdx, ReqAdminBoardDeletedDataDTO reqAdminBoardDeletedDataDTO,
            CustomUserDetails customUserDetails) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if (!boardEntityOptional.isPresent()) {
            throw new BadRequestException("게시물 정보가 없습니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setDeletedAt(LocalDateTime.now());

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("board")
                .columnName("deleted_at")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(boardIdx)
                .operation("DELETE")
                .reason(reqAdminBoardDeletedDataDTO.getReason())
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("게시물 삭제 처리 완료")
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateBoardRestoreData(Long boardIdx, CustomUserDetails customUserDetails){
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByIdx(boardIdx);

        if(!boardEntityOptional.isPresent()){
            throw new BadRequestException("게시물 정보가 없습니다.");
        }

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setDeletedAt(null);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("board")
                .columnName("deleted_at")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(boardIdx)
                .operation("UPDATE")
                .newValue(null)
                .reason(null)
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("게시물 삭제 복구 완료")
                        .build(),
                HttpStatus.OK);
    }
}