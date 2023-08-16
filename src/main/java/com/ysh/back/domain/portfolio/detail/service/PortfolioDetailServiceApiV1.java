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
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailPerchaseDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailSellDTO;
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
    public ResponseEntity<?> postPortfolioDetailPurchase(
            ReqPostPortfolioDetailPerchaseDTO reqPostPortfolioDetailPerchaseDTO,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository
                .findByIdx(reqPostPortfolioDetailPerchaseDTO.getPortfolioIdx());
        if (!portfolioEntityOptional.isPresent()) {
            throw new BadRequestException("해당 포트폴리오를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();

        Optional<AssetEntity> assetEntityOptional = assetRepository
                .findByIdx(reqPostPortfolioDetailPerchaseDTO.getAssetIdx());
        if (!assetEntityOptional.isPresent()) {
            throw new BadRequestException("해당 자산 정보를 찾을 수 없습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();

        String message;

        Optional<PortfolioDetailEntity> portfolioDetailEntityOptional = portfolioDetailRepository
                .findByPortfolioEntityIdxAndAssetEntityIdx(reqPostPortfolioDetailPerchaseDTO.getPortfolioIdx(),
                        reqPostPortfolioDetailPerchaseDTO.getAssetIdx());
        if (portfolioDetailEntityOptional.isPresent()) {

            PortfolioDetailEntity portfolioDetailEntity = portfolioDetailEntityOptional.get();
            portfolioDetailEntity.setAmount(portfolioDetailEntity.getAmount()
                    .add(reqPostPortfolioDetailPerchaseDTO.getAmount()));
            portfolioDetailEntity.setAveragePurchasePrice(
                    portfolioDetailEntity.getAveragePurchasePrice()
                            .add(reqPostPortfolioDetailPerchaseDTO.getAveragePurchasePrice())
                            .divide(new BigDecimal("2.0"), 2, RoundingMode.HALF_UP));
            portfolioDetailEntity
                    .setTotalPurchasePrice(portfolioDetailEntity.getTotalPurchasePrice()
                            .add(reqPostPortfolioDetailPerchaseDTO.getTotalPurchasePrice()));
            portfolioDetailEntity.setDividendMonth(reqPostPortfolioDetailPerchaseDTO.getDivedendMonth());
            portfolioDetailEntity
                    .setDividendAmount(reqPostPortfolioDetailPerchaseDTO.getDivendedAmount());

            AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("portfolio_detail")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                    .operation("UPDATE")
                    .newValue(portfolioDetailEntity.getAssetEntity().getName())
                    .reason("포트폴리오 세부목록 수정")
                    .build();

            auditLogRepository.save(auditLog);
            message = "포트폴리오 세부 목록 수정완료";
        } else {
            PortfolioDetailEntity portfolioDetailEntityForSaving = PortfolioDetailEntity.builder()
                    .portfolioEntity(portfolioEntity)
                    .assetEntity(assetEntity)
                    .amount(reqPostPortfolioDetailPerchaseDTO.getAmount())
                    .averagePurchasePrice(reqPostPortfolioDetailPerchaseDTO.getAveragePurchasePrice())
                    .totalPurchasePrice(reqPostPortfolioDetailPerchaseDTO.getTotalPurchasePrice())
                    .dividendMonth(reqPostPortfolioDetailPerchaseDTO.getDivedendMonth())
                    .dividendAmount(reqPostPortfolioDetailPerchaseDTO.getDivendedAmount())
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

    @Transactional
    public ResponseEntity<?> postPortfolioDetailSell(
            ReqPostPortfolioDetailSellDTO reqPostPortfolioDetailSellDTO,
            CustomUserDetails customUserDetails) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername()); 
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(reqPostPortfolioDetailSellDTO.getPortfolioIdx());
        if(!portfolioEntityOptional.isPresent()){
            throw new BadRequestException("해당 포트폴리오 정보를 찾을 수 없습니다.");
        }

        Optional<PortfolioDetailEntity> portfolioDetailEntityOptional = portfolioDetailRepository
                .findByPortfolioEntityIdxAndAssetEntityIdx(reqPostPortfolioDetailSellDTO.getPortfolioIdx(),
                        reqPostPortfolioDetailSellDTO.getAssetIdx());
        if (!portfolioDetailEntityOptional.isPresent()) {
            throw new BadRequestException("해당 포트폴리오 상세 정보를 찾을 수 없습니다.");
        }
        PortfolioDetailEntity portfolioDetailEntity = portfolioDetailEntityOptional.get();

        int compareResult = portfolioDetailEntity.getAmount().compareTo(reqPostPortfolioDetailSellDTO.getAmount());
        if (compareResult == -1) {
            throw new BadRequestException("보유량이 충분하지 않습니다.");
        } else if (compareResult == 0) {

            AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("portfolioDetail")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                    .operation("DELETE")
                    .oldValue(portfolioDetailEntity.getAssetEntity().getName())
                    .reason(portfolioDetailEntity.getAssetEntity().getName() + "종목"
                    + reqPostPortfolioDetailSellDTO.getAmount() + "개 매도(전량 매도)")
                    .build();

            portfolioDetailRepository.delete(portfolioDetailEntity);

            auditLogRepository.save(auditLog);

            return new ResponseEntity<>(
                    ResponseDTO.builder()
                            .code(0)
                            .message("판매 성공!")
                            .build(),
                    HttpStatus.OK);

        } else {
            BigDecimal newTotalPurchase = portfolioDetailEntity.getTotalPurchasePrice().subtract(reqPostPortfolioDetailSellDTO.getTotalSellPrice());
            BigDecimal newAmount = portfolioDetailEntity.getAmount().subtract(reqPostPortfolioDetailSellDTO.getAmount());
            BigDecimal newAvgPurchase = portfolioDetailEntity.getAveragePurchasePrice().add(newTotalPurchase.divide(newAmount)).divide(new BigDecimal(2));

            portfolioDetailEntity.setTotalPurchasePrice(newTotalPurchase);
            portfolioDetailEntity.setAmount(newAmount);
            portfolioDetailEntity.setAveragePurchasePrice(newAvgPurchase);

             AuditLogEntity auditLog = AuditLogEntity.builder()
                    .tableName("portfolioDetail")
                    // 유저 정보 가져올 수 있을 때 바꾸자.
                    .userIdx(userEntity.getIdx())
                    .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                    .operation("UPDATE")
                    .oldValue(portfolioDetailEntity.getAmount().toString())
                    .newValue(newAmount.toString())
                    .reason(portfolioDetailEntity.getAssetEntity().getName() + "종목"
                    + reqPostPortfolioDetailSellDTO.getAmount() + "개 매도")
                    .build();

            auditLogRepository.save(auditLog);

            return new ResponseEntity<>(
                    ResponseDTO.builder()
                            .code(0)
                            .message("판매 성공!")
                            .build(),
                    HttpStatus.OK);
        }
    }
}
