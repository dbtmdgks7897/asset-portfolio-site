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
import com.ysh.back.domain.asset.dto.ReqAssetPurchaseDTO;
import com.ysh.back.domain.asset.dto.ReqAssetSellDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.dto.ResAssetDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailPerchaseDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailSellDTO;
import com.ysh.back.domain.portfolio.detail.service.PortfolioDetailServiceApiV1;
import com.ysh.back.domain.transaction.dto.ReqPostTransactionDTO;
import com.ysh.back.domain.transaction.service.TransactionServiceApiV1;
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
    @Autowired
    TransactionServiceApiV1 transactionServiceApiV1;
    @Autowired
    PortfolioDetailServiceApiV1 portfolioDetailServiceApiV1;

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

    public ResponseEntity<?> purchaseAsset(ReqAssetPurchaseDTO reqAssetPurchaseDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        ReqPostAssetDTO reqPostAssetDTO = ReqPostAssetDTO.builder()
        .assetCode(reqAssetPurchaseDTO.getAsset().getIdx())
        .assetName(reqAssetPurchaseDTO.getAsset().getName())
        .assetType(reqAssetPurchaseDTO.getAsset().getType())
        .build();

        postAssetData(reqPostAssetDTO, customUserDetails);
        
        ReqPostPortfolioDetailPerchaseDTO tempReqPostPortfolioDetailPerchaseDTO = ReqPostPortfolioDetailPerchaseDTO.builder()
        .assetIdx(reqAssetPurchaseDTO.getAsset().getIdx())
        .portfolioIdx(reqAssetPurchaseDTO.getPortfolioDetail().getPortfolioIdx())
        .amount(reqAssetPurchaseDTO.getPortfolioDetail().getAmount())
        .averagePurchasePrice(reqAssetPurchaseDTO.getPortfolioDetail().getAveragePurchasePrice())
        .totalPurchasePrice(reqAssetPurchaseDTO.getPortfolioDetail().getTotalPurchasePrice())
        .build();

        portfolioDetailServiceApiV1.postPortfolioDetailPurchase(tempReqPostPortfolioDetailPerchaseDTO, customUserDetails);


        ReqPostTransactionDTO tempReqPostTransactionDTO = ReqPostTransactionDTO.builder()
        .assetIdx(reqAssetPurchaseDTO.getAsset().getIdx())
        .portfolioIdx(reqAssetPurchaseDTO.getPortfolioDetail().getPortfolioIdx())
        .type(reqAssetPurchaseDTO.getTransaction().getType())
        .amount(reqAssetPurchaseDTO.getTransaction().getAmount())
        .priceAvg(reqAssetPurchaseDTO.getTransaction().getPriceAvg())
        .profit(reqAssetPurchaseDTO.getTransaction().getProfit())
        .build();

        transactionServiceApiV1.postTransaction(tempReqPostTransactionDTO, customUserDetails);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("구매 성공")
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> sellAsset(ReqAssetSellDTO reqAssetSellDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqAssetSellDTO.getAssetIdx());
        if(!assetEntityOptional.isPresent()){
            throw new BadRequestException("자산 정보를 찾을 수 없습니다.");
        }
        
        ReqPostPortfolioDetailSellDTO reqPostPortfolioDetailSellDTO = ReqPostPortfolioDetailSellDTO.builder()
        .portfolioIdx(reqAssetSellDTO.getPortfolioDetail().getPortfolioIdx())
        .assetIdx(reqAssetSellDTO.getAssetIdx())
        .amount(reqAssetSellDTO.getPortfolioDetail().getAmount())
        .totalSellPrice(reqAssetSellDTO.getPortfolioDetail().getTotalSellPrice())
        .build();

        portfolioDetailServiceApiV1.postPortfolioDetailSell(reqPostPortfolioDetailSellDTO, customUserDetails);

        ReqPostTransactionDTO reqPostTransactionDTO = ReqPostTransactionDTO.builder()
        .assetIdx(reqAssetSellDTO.getAssetIdx())
        .portfolioIdx(reqAssetSellDTO.getPortfolioDetail().getPortfolioIdx())
        .type(reqAssetSellDTO.getTransaction().getType())
        .amount(reqAssetSellDTO.getTransaction().getAmount())
        .priceAvg(reqAssetSellDTO.getTransaction().getPriceAvg())
        .profit(reqAssetSellDTO.getTransaction().getAmount().multiply(reqAssetSellDTO.getTransaction().getPriceAvg()))
        .build();

        transactionServiceApiV1.postTransaction(reqPostTransactionDTO, customUserDetails);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("판매 성공")
                        .build(),
                HttpStatus.OK);

    }
}
