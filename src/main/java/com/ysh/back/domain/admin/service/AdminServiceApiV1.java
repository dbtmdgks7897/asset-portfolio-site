package com.ysh.back.domain.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.domain.admin.dto.ResAdminUserInitDTO;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class AdminServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> getAdminUserData(){
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

    public ResponseEntity<?> getAdminUserSearchData(String search){
        List<UserEntity> userEntityList;
        Long searchLong = -1L;
        try {
            searchLong = Long.parseLong(search);
        } catch (Exception e) {
            
        } finally {
            userEntityList = userRepository.findByIdxOrEmailContainingOrNicknameContaining(searchLong, search, search);
        }
        
        
        if(userEntityList.isEmpty()){
            throw new BadRequestException("검색 결과 없음");
        }

        ResAdminUserInitDTO resAdminUserInitDTO = ResAdminUserInitDTO.of(userEntityList);

        return new ResponseEntity<>(
            ResponseDTO.builder()
            .code(0)
            .message("유저 검색 성공")
            .data(resAdminUserInitDTO)
            .build(),
            HttpStatus.OK
        );
    }
}