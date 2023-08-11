package com.ysh.back.domain.mypage.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.mypage.dto.ReqMyinfoUpdateDTO;
import com.ysh.back.domain.mypage.dto.ResMyactiveDTO;
import com.ysh.back.domain.mypage.dto.ResMyinfoInitDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.board.repository.BoardRepository;
import com.ysh.back.model.comment.entity.CommentEntity;
import com.ysh.back.model.comment.repository.CommentRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class MypageServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public ResponseEntity<?> getMyinfoInitData(CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());

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
    public ResponseEntity<?> updateMyinfoData(ReqMyinfoUpdateDTO reqMyinfoUpdateDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("해당 사용자가 존재하지 않습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();

        String oldVal =
        "nickname : " + userEntity.getNickname() + ", " +
        "phone : " + userEntity.getPhone() + ", " +
        "gender : " + userEntity.getGender() + ", " +
        "age : " + userEntity.getAge() + ", " +
        "img-type : " + userEntity.getImgType() + ", ";

        // TODO : 이미지 업로드

        String filePath;
        String fileName = "profile_" + customUserDetails.getLoginUserDTO().getUser().getIdx() + "." + reqMyinfoUpdateDTO.getFile().getContentType().split("/")[1];
        try {
            
            filePath = uploadPath + File.separator + fileName;
            reqMyinfoUpdateDTO.getFile().transferTo(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("파일 업로드 오류");
        }

        // String imgBase64 = Base64.getEncoder().encodeToString(reqMyinfoUpdateDTO.getProfileImg().getBytes());
        // String imgUrl = "data:"+ reqMyinfoUpdateDTO.getProfileImg().getContentType() + ";base64," + imgBase64;

        userEntity.setProfileImg("/upload/"+ fileName);
        // userEntity.setImgType(reqMyinfoUpdateDTO.getImgType());
        userEntity.setNickname(reqMyinfoUpdateDTO.getNickname());
        userEntity.setGender(reqMyinfoUpdateDTO.getGender());
        userEntity.setAge(reqMyinfoUpdateDTO.getAge());
        userEntity.setPhone(reqMyinfoUpdateDTO.getPhone());

        String newVal =
        "nickname : " + userEntity.getNickname() + ", " +
        "phone : " + userEntity.getPhone() + ", " +
        "gender : " + userEntity.getGender() + ", " +
        "age : " + userEntity.getAge() + ", " +
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
    public ResponseEntity<?> deleteMyinfoData(CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());

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
            .data(null)
            .build()
        ,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getMyactiveData(CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());

        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("유저 정보가 존재하지 않습니다.");
        }

        UserEntity userEntity = userEntityOptional.get();

        List<BoardEntity> boardEntityList = boardRepository.findAllByUserEntity_IdxOrderByCreatedAtDesc(userEntity.getIdx());
        List<CommentEntity> commentEntityList = commentRepository.findAllByUserEntity_IdxOrderByCreatedAtDesc(userEntity.getIdx());

        ResMyactiveDTO resMyactiveDTO = ResMyactiveDTO.of(boardEntityList, commentEntityList);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("활동 조회 성공")
            .data(resMyactiveDTO)
            .build(),
            HttpStatus.OK
        );        
    }
}
