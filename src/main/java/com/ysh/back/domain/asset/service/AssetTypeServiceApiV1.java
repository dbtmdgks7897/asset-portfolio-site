package com.ysh.back.domain.asset.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.dto.ResGetAssetTypeListDTO;
import com.ysh.back.model.asset.entity.AssetTypeEntity;
import com.ysh.back.model.asset.repository.AssetTypeRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class AssetTypeServiceApiV1 {
    
    @Autowired
    AssetTypeRepository assetTypeRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> getAssetTypeList(CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        List<AssetTypeEntity> assetTypeEntityList = assetTypeRepository.findAll();
        ResGetAssetTypeListDTO dto = ResGetAssetTypeListDTO.of(assetTypeEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("자산 타입 조회 성공")
                        .data(dto)
                        .build(),
                HttpStatus.OK);

    }

}
