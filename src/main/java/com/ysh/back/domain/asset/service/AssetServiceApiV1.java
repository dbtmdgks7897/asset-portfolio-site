package com.ysh.back.domain.asset.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.dto.ApiGetDomesticStockNameDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.dto.ResAssetDTO;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetServiceApiV1 {

        @Autowired
        AuditLogRepository auditLogRepository;
        @Autowired
        AssetRepository assetRepository;
        @Autowired
        UserRepository userRepository;

        @Transactional
        public ResponseEntity<?> postAssetData(ReqPostAssetDTO reqPostAssetDTO, CustomUserDetails customUserDetails) {

                Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
                if (!userEntityOptional.isPresent()) {
                        throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
                }
                UserEntity userEntity = userEntityOptional.get();

                Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqPostAssetDTO.getAssetCode());
                if (assetEntityOptional.isPresent()) {
                        ResAssetDTO dto = ResAssetDTO.builder()
                                        .assetCode(assetEntityOptional.get().getIdx())
                                        .assetName(assetEntityOptional.get().getName())
                                        .assetType(assetEntityOptional.get().getType())
                                        .build();

                        return new ResponseEntity<>(
                                        ResponseDTO.builder()
                                                        .code(0)
                                                        .message("이미 존재하는 자산")
                                                        .data(dto)
                                                        .build(),
                                        HttpStatus.OK);
                }

                

                AssetEntity assetEntityForSaving = AssetEntity.builder()
                                .idx(reqPostAssetDTO.getAssetCode())
                                .name(reqPostAssetDTO.getAssetName())
                                .type(reqPostAssetDTO.getAssetType())
                                .build();

                AssetEntity assetEntity = assetRepository.save(assetEntityForSaving);

                ResAssetDTO dto = ResAssetDTO.builder()
                                .assetCode(assetEntity.getIdx())
                                .assetName(assetEntity.getName())
                                .assetType(assetEntity.getType())
                                .build();

                AuditLogEntity auditLog = AuditLogEntity.builder()
                                .tableName("asset")
                                // 유저 정보 가져올 수 있을 때 바꾸자.
                                .userIdx(userEntity.getIdx())
                                .operation("INSERT")
                                .oldValue("")
                                .newValue(dto.getAssetName())
                                .reason("새로운 자산 검색")
                                .build();

                auditLogRepository.save(auditLog);

                return new ResponseEntity<>(
                                ResponseDTO.builder()
                                                .code(0)
                                                .message("자산 등록 성공")
                                                .data(dto)
                                                .build(),
                                HttpStatus.OK);
        }

}