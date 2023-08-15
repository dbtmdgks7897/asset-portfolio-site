package com.ysh.back.domain.portfolio.detail.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailDTO;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;
import com.ysh.back.model.portfolio.repository.PortfolioDetailRepository;
import com.ysh.back.model.portfolio.repository.PortfolioRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class PortfolioDetailServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    PortfolioDetailRepository portfolioDetailRepository;
    @Autowired
    AuditLogRepository auditLogRepository;

    public ResponseEntity<?> postPortfolioDetail(ReqPostPortfolioDetailDTO reqPostPortfolioDetailDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername()); 
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(reqPostPortfolioDetailDTO.getPortfolioIdx());
        if(!portfolioEntityOptional.isPresent()){
            throw new BadRequestException("해당 포트폴리오를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();

        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqPostPortfolioDetailDTO.getAssetIdx());
        if(!assetEntityOptional.isPresent()){
            throw new BadRequestException("해당 자산 정보를 찾을 수 없습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();

        PortfolioDetailEntity portfolioDetailEntityForSaving = PortfolioDetailEntity.builder()
        .portfolioEntity(portfolioEntity)
        .assetEntity(assetEntity)
        .amount(reqPostPortfolioDetailDTO.getAmount())
        .averagePurchasePrice(reqPostPortfolioDetailDTO.getAveragePurchasePrice())
        .totalPurchasePrice(reqPostPortfolioDetailDTO.getTotalPurchasePrice())
        .dividendMonth(reqPostPortfolioDetailDTO.getDivendedAmount())
        .dividendAmount(reqPostPortfolioDetailDTO.getDivendedAmount())
        .build();

        PortfolioDetailEntity portfolioDetailEntity = portfolioDetailRepository.save(portfolioDetailEntityForSaving);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("portfolio")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userEntity.getIdx())
                .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                .operation("INSERT")
                .reason("포트폴리오 세부목록 생성")
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("새로운 포트폴리오 세부목록이 추가되었습니다.")
                        .build(),
                HttpStatus.OK);
    }

}
