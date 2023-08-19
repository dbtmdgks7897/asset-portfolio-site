package com.ysh.back.domain.portfolio.detail.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.detail.dto.ReqDeletePortfolioDetailDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailPerchaseDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailSellDTO;
import com.ysh.back.domain.portfolio.detail.dto.ResGetPortfolioDetailDTO;
import com.ysh.back.domain.portfolio.dto.chart.ChartDataDTO;
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
    public ResponseEntity<?> getDetailChart(Integer portfolioIdx, CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(portfolioIdx);
        if (!portfolioEntityOptional.isPresent()) {
            throw new BadRequestException("포트폴리오 정보를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();
        List<PortfolioDetailEntity> portfolioDetailEntityList = portfolioEntity.getPortfolioDetailEntityList();
        List<PortfolioDetailEntity> notNullEntityList = portfolioDetailEntityList.stream()
                .filter(entity -> entity.getDeletedAt() == null)
                .toList();

        List<String> stockLabelList = new ArrayList<String>();
        List<BigDecimal> stockPriceList = new ArrayList<BigDecimal>();
        List<String> currencyLabelList = new ArrayList<String>();
        List<BigDecimal> currencyPriceList = new ArrayList<BigDecimal>();
        List<String> bitcoinLabelList = new ArrayList<String>();
        List<BigDecimal> bitcoinPriceList = new ArrayList<BigDecimal>();
        for (PortfolioDetailEntity portfolioDetailEntity : notNullEntityList) {
            String typeName = portfolioDetailEntity.getAssetEntity().getAssetTypeEntity().getName();
            if (typeName.startsWith("주식")) {
                stockLabelList.add(portfolioDetailEntity.getAssetEntity().getName());
                stockPriceList.add(portfolioDetailEntity.getTotalPurchasePrice());
            } else if (typeName.equals("외화")) {
                currencyLabelList.add(portfolioDetailEntity.getAssetEntity().getName());
                currencyPriceList.add(portfolioDetailEntity.getTotalPurchasePrice());
            } else if (typeName.equals("암호화폐")) {
                bitcoinLabelList.add(portfolioDetailEntity.getAssetEntity().getName());
                bitcoinPriceList.add(portfolioDetailEntity.getTotalPurchasePrice());
            }
        }

        ChartDataDTO stockChartDataDTO = ChartDataDTO.of(stockLabelList, stockPriceList);
        ChartDataDTO currencyChartDataDTO = ChartDataDTO.of(currencyLabelList, currencyPriceList);
        ChartDataDTO bitcoinChartDataDTO = ChartDataDTO.of(bitcoinLabelList, bitcoinPriceList);

        HashMap<String, ChartDataDTO> result = new HashMap<String, ChartDataDTO>();
        result.put("주식", stockChartDataDTO);
        result.put("외화", currencyChartDataDTO);
        result.put("암호화폐", bitcoinChartDataDTO);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("포트폴리오 디테일 차트 가져오기 성공")
                        .data(result)
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> getDetailList(Integer portfolioIdx, CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(portfolioIdx);
        if (!portfolioEntityOptional.isPresent()) {
            throw new BadRequestException("포트폴리오 정보를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();
        if (userEntity.getIdx() != portfolioEntity.getUserEntity().getIdx()) {
            throw new BadRequestException("포트폴리오 접근 권한을 찾을 수 없습니다.");
        }

        List<PortfolioDetailEntity> portfolioDetailEntityList = portfolioEntity.getPortfolioDetailEntityList();
        List<PortfolioDetailEntity> result = portfolioDetailEntityList.stream()
                .filter(entity -> entity.getDeletedAt() == null)
                .toList();

        ResGetPortfolioDetailDTO dto = ResGetPortfolioDetailDTO.of(result);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("포트폴리오 디테일 리스트 가져오기 성공")
                        .data(dto)
                        .build(),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteDetail(Integer portfolioIdx, ReqDeletePortfolioDetailDTO reqDeletePortfolioDetailDTO,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioDetailEntity> portfolioDetailEntityOptional = portfolioDetailRepository
                .findByPortfolioEntityIdxAndAssetEntityIdx(portfolioIdx,
                        reqDeletePortfolioDetailDTO.getCode());
        System.out.println("IDX : " + portfolioIdx + "CODE :" + reqDeletePortfolioDetailDTO.getCode());
        if (!portfolioDetailEntityOptional.isPresent()) {
            throw new BadRequestException("포트폴리오 상세 정보를 찾을 수 없습니다.");
        }
        PortfolioDetailEntity portfolioDetailEntity = portfolioDetailEntityOptional.get();

        portfolioDetailEntity.setDeletedAt(LocalDateTime.now());
        portfolioDetailEntity.setAmount(new BigDecimal(0));
        portfolioDetailEntity.setAveragePurchasePrice(new BigDecimal(0));
        portfolioDetailEntity.setTotalPurchasePrice(new BigDecimal(0));

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("portfolio_detail")
                .userIdx(userEntity.getIdx())
                .rowId(Long.valueOf(portfolioDetailEntity.getIdx()))
                .operation("DELETE")
                .oldValue(portfolioDetailEntity.getAssetEntity().getName())
                .reason("포트폴리오 세부목록 삭제")
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("포트폴리오 상세 정보 삭제 성공")
                        .build(),
                HttpStatus.OK);
    }

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

            portfolioDetailEntity.setDeletedAt(null);

            BigDecimal newTotalPurchase = portfolioDetailEntity.getTotalPurchasePrice()
                    .add(reqPostPortfolioDetailPerchaseDTO.getTotalPurchasePrice());
            BigDecimal newAmount = portfolioDetailEntity.getAmount()
                    .add(reqPostPortfolioDetailPerchaseDTO.getAmount());
            BigDecimal newAvgPurchase = newTotalPurchase.divide(newAmount, 2, RoundingMode.HALF_UP);

            portfolioDetailEntity.setAmount(newAmount);
            portfolioDetailEntity.setAveragePurchasePrice(newAvgPurchase);
            portfolioDetailEntity
                    .setTotalPurchasePrice(newTotalPurchase);
            portfolioDetailEntity.setDividendMonth(reqPostPortfolioDetailPerchaseDTO.getDivedendMonth());
            portfolioDetailEntity
                    .setDividendAmount(reqPostPortfolioDetailPerchaseDTO.getDivendedAmount());
            LocalDateTime current = LocalDateTime.now();
            portfolioDetailEntity.setUpdatedAt(current);
            
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
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository
                .findByIdx(reqPostPortfolioDetailSellDTO.getPortfolioIdx());
        if (!portfolioEntityOptional.isPresent()) {
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
        System.out.println("");
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

            portfolioDetailEntity.setAmount(new BigDecimal(0));
            portfolioDetailEntity.setTotalPurchasePrice(new BigDecimal(0));
            portfolioDetailEntity.setDeletedAt(LocalDateTime.now());

            auditLogRepository.save(auditLog);

            return new ResponseEntity<>(
                    ResponseDTO.builder()
                            .code(0)
                            .message("판매 성공!")
                            .build(),
                    HttpStatus.OK);
        } else {
            BigDecimal newTotalPurchase = portfolioDetailEntity.getTotalPurchasePrice()
                    .subtract(reqPostPortfolioDetailSellDTO.getTotalSellPrice());
            BigDecimal newAmount = portfolioDetailEntity.getAmount()
                    .subtract(reqPostPortfolioDetailSellDTO.getAmount());
            BigDecimal newAvgPurchase = newTotalPurchase.divide(newAmount, 2, RoundingMode.HALF_UP);

            portfolioDetailEntity.setTotalPurchasePrice(newTotalPurchase);
            portfolioDetailEntity.setAmount(newAmount);
            portfolioDetailEntity.setAveragePurchasePrice(newAvgPurchase);
            LocalDateTime current = LocalDateTime.now();
            portfolioDetailEntity.setUpdatedAt(current);

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