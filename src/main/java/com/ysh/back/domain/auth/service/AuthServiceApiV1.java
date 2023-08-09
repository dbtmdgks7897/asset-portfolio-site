package com.ysh.back.domain.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.auth.dto.ReqJoinDTO;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class AuthServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Transactional
    public ResponseEntity<?> join(ReqJoinDTO dto) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(dto.getUser().getEmail());

        if (userEntityOptional.isPresent()) {
            throw new BadRequestException("이미 존재하는 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getUser().getPassword());
        UserEntity userEntityForSaving = UserEntity.builder()
                .email(dto.getUser().getEmail())
                .password(encodedPassword)
                .name(dto.getUser().getName())
                .nickname(dto.getUser().getNickname())
                .phone(dto.getUser().getPhone())
                .build();

        userRepository.save(userEntityForSaving);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("회원가입에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

}
