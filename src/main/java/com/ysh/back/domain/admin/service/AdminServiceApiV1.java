package com.ysh.back.domain.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.admin.dto.ReqAdminUserDeletedDataDTO;
import com.ysh.back.domain.admin.dto.ReqAdminUserSuspendData;
import com.ysh.back.domain.admin.dto.ResAdminUserInitDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuditLogRepository auditLogRepository;

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
                .operation("UPDATE")
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
        userEntity.setSuspendReason(null);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("suspend_until")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("UPDATE")
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
        userEntity.setDeletedReason(null);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("user")
                .columnName("deleted_at")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userRepository.findByEmail(customUserDetails.getUsername()).get().getIdx())
                .rowId(userIdx)
                .operation("UPDATE")
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
}