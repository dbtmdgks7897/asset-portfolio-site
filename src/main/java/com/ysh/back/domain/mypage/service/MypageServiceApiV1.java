package com.ysh.back.domain.mypage.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.domain.mypage.dto.ReqMyinfoInitDTO;
import com.ysh.back.domain.mypage.dto.ReqMyinfoUpdateDTO;
import com.ysh.back.domain.mypage.dto.ResMyinfoInitDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class MypageServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    
    public ResponseEntity<?> getMyinfoInitData(ReqMyinfoInitDTO reqMyinfoInitDTO){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(reqMyinfoInitDTO.getIdx());

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("님 누구세요?");
        }
        UserEntity userEntity = userEntityOptional.get();

        ResMyinfoInitDTO resMyinfoInitDTO = ResMyinfoInitDTO.builder()
        .nickname(userEntity.getNickname())
        .name(userEntity.getName())
        .gender(userEntity.getGender())
        .age(userEntity.getAge())
        .phone(userEntity.getPhone())
        .profileImg(userEntity.getProfileImg())
        .imgType(userEntity.getImgType())
        .email(userEntity.getEmail())
        .build();

        
        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("내 정보 조회 성공")
            .data(resMyinfoInitDTO)
            .build(),
            HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<?> updateMyinfoData(ReqMyinfoUpdateDTO reqMyinfoUpdateDTO){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(reqMyinfoUpdateDTO.getIdx());

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("해당 사용자가 존재하지 않습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();

        String oldVal =
        "nickname : " + userEntity.getNickname() + ", " +
        "phone : " + userEntity.getPhone() + ", " +
        "gender : " + userEntity.getGender() + ", " +
        "age : " + userEntity.getAge() + ", " +
        "profile-img : " + userEntity.getProfileImg() + ", " +
        "img-type : " + userEntity.getImgType() + ", ";

        // userEntity.setProfileImg(reqMyinfoUpdateDTO.getProfileImg());
        userEntity.setImgType(reqMyinfoUpdateDTO.getImgType());
        userEntity.setNickname(reqMyinfoUpdateDTO.getNickname());
        userEntity.setGender(reqMyinfoUpdateDTO.getGender());
        userEntity.setAge(reqMyinfoUpdateDTO.getAge());
        userEntity.setPhone(reqMyinfoUpdateDTO.getPhone());

        String newVal =
        "nickname : " + userEntity.getNickname() + ", " +
        "phone : " + userEntity.getPhone() + ", " +
        "gender : " + userEntity.getGender() + ", " +
        "age : " + userEntity.getAge() + ", " +
        "profile-img : " + userEntity.getProfileImg() + ", " +
        "img-type : " + userEntity.getImgType() + ", ";

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("user")
        .userIdx(userEntity.getIdx())
        .rowId(userEntity.getIdx())
        .operation("UPDATE")
        .oldValue(oldVal)
        .newValue(newVal)
        .reason("사용자 정보 변경")
        .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("내 정보 수정 성공")
            .build(),
            HttpStatus.OK
        );
    }
    
    @Transactional
    public ResponseEntity<?> deleteMyinfoData(Long userIdx){
        Optional<UserEntity> userEntityOptional = userRepository.findByIdx(userIdx);

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("해당 사용자가 존재하지 않습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();
        
        LocalDateTime now = LocalDateTime.now();

        userEntity.setDeletedAt(now);

        AuditLogEntity auditLog = AuditLogEntity.builder()
        .tableName("user")
        .userIdx(userEntity.getIdx())
        .rowId(userEntity.getIdx())
        .operation("DELETE")
        .reason("사용자 회원 탈퇴")
        .build();   

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("GOOD BYE!")
            .build()
        ,HttpStatus.OK);
    }
}
