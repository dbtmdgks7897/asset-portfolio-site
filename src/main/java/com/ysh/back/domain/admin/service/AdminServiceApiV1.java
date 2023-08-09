package com.ysh.back.domain.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.admin.dto.ResAdminUserInitDTO;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class AdminServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> getAdminUserData(CustomUserDetails customUserDetails){

        System.out.println("낑낑");

        List<UserEntity> userEntityList = userRepository.findAll();
        
        ResAdminUserInitDTO resAdminUserInitDTO = ResAdminUserInitDTO.of(userEntityList);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("유저 리스트 조회 성공")
            .data(resAdminUserInitDTO)
            .build(),
            HttpStatus.OK
        );
    }
}