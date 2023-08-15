package com.ysh.back.domain.portfolio.detail.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import jakarta.transaction.Transactional;

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

    @Transactional
    public ResponseEntity<?> postPortfolioDetail(ReqPostPortfolioDetailDTO reqPostPortfolioDetailDTO,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository
                .findByIdx(reqPostPortfolioDetailDTO.getPortfolioIdx());
        if (!portfolioEntityOptional.isPresent()) {
            throw new BadRequestException("해당 포트폴리오를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();

        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqPostPortfolioDetailDTO.getAssetIdx());
        if (!assetEntityOptional.isPresent()) {
            throw new BadRequestException("해당 자산 정보를 찾을 수 없습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();

        String message;

        Optional<PortfolioDetailEntity> portfolioDetailEntityOptional = portfolioDetailRepository
                .findByPortfolioEntityIdxAndAssetEntityIdx(reqPostPortfolioDetailDTO.getPortfolioIdx(),
                        reqPostPortfolioDetailDTO.getAssetIdx());
        if (portfolioDetailEntityOptional.isPresent()) {
            PortfolioDetailEntity portfolioDetailEntityForUpdating = portfolioDetailEntityOptional.get();
            portfolioDetailEntityForUpdating.setAmount(portfolioDetailEntityForUpdating.getAmount().add(reqPostPortfolioDetailDTO.getAmount()));
            portfolioDetailEntityForUpdating.setAveragePurchasePrice(portfolioDetailEntityForUpdating.getAveragePurchasePrice().add(reqPostPortfolioDetailDTO.getAveragePurchasePrice()).divide(new BigDecimal("2.0"), 2, RoundingMode.HALF_UP));
            portfolioDetailEntityForUpdating.setTotalPurchasePrice(portfolioDetailEntityForUpdating.getTotalPurchasePrice().add(reqPostPortfolioDetailDTO.getTotalPurchasePrice()));
            portfolioDetailEntityForUpdating.setDividendMonth(reqPostPortfolioDetailDTO.getDivedendMonth());
            portfolioDetailEntityForUpdating.setDividendAmount(reqPostPortfolioDetailDTO.getDivendedAmount());

                    AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("portfolio_detail")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(portfolioDetailEntityForUpdating.getIdx()))
                    .operation("UPDATE")
                    .newValue(portfolioDetailEntityForUpdating.getAssetEntity().getName())
                    .reason("포트폴리오 세부목록 수정")
                    .build();

            auditLogRepository.save(auditLog);
            message = "포트폴리오 세부 목록 수정완료";
        } else {
            PortfolioDetailEntity portfolioDetailEntityForSaving = PortfolioDetailEntity.builder()
                    .portfolioEntity(portfolioEntity)
                    .assetEntity(assetEntity)
                    .amount(reqPostPortfolioDetailDTO.getAmount())
                    .averagePurchasePrice(reqPostPortfolioDetailDTO.getAveragePurchasePrice())
                    .totalPurchasePrice(reqPostPortfolioDetailDTO.getTotalPurchasePrice())
                    .dividendMonth(reqPostPortfolioDetailDTO.getDivedendMonth())
                    .dividendAmount(reqPostPortfolioDetailDTO.getDivendedAmount())
                    .build();

            PortfolioDetailEntity portfolioDetailEntity = portfolioDetailRepository
                    .save(portfolioDetailEntityForSaving);

            AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("portfolio_detail")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                    .operation("INSERT")
                    .newValue(portfolioDetailEntity.getAssetEntity().getName())
                    .reason("포트폴리오 세부목록 생성")
                    .build();

            auditLogRepository.save(auditLog);
            message = "포트폴리오 세부 목록 생성완료";
        }
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message(message)
                        .build(),
                HttpStatus.OK);
    }

}
