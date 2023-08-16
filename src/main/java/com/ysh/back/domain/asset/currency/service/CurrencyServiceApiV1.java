package com.ysh.back.domain.asset.currency.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.currency.dto.ReqCurrencyPurchaseDTO;
import com.ysh.back.domain.asset.currency.dto.ReqCurrencySellDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailPerchaseDTO;
import com.ysh.back.domain.portfolio.detail.dto.ReqPostPortfolioDetailSellDTO;
import com.ysh.back.domain.portfolio.detail.service.PortfolioDetailServiceApiV1;
import com.ysh.back.domain.transaction.dto.ReqPostTransactionDTO;
import com.ysh.back.domain.transaction.service.TransactionServiceApiV1;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class CurrencyServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    AssetServiceApiV1 assetServiceApiV1;
    @Autowired
    TransactionServiceApiV1 transactionServiceApiV1;
    @Autowired
    PortfolioDetailServiceApiV1 portfolioDetailServiceApiV1;

    public ResponseEntity<?> purchaseCurrency(ReqCurrencyPurchaseDTO reqCurrencyPurchaseDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        ReqPostAssetDTO reqPostAssetDTO = ReqPostAssetDTO.builder()
        .assetCode(reqCurrencyPurchaseDTO.getAsset().getIdx())
        .assetName(reqCurrencyPurchaseDTO.getAsset().getName())
        .assetType(reqCurrencyPurchaseDTO.getAsset().getType())
        .build();

        assetServiceApiV1.postAssetData(reqPostAssetDTO, customUserDetails);
        
        ReqPostPortfolioDetailPerchaseDTO tempReqPostPortfolioDetailPerchaseDTO = ReqPostPortfolioDetailPerchaseDTO.builder()
        .assetIdx(reqCurrencyPurchaseDTO.getAsset().getIdx())
        .portfolioIdx(reqCurrencyPurchaseDTO.getPortfolioDetail().getPortfolioIdx())
        .amount(reqCurrencyPurchaseDTO.getPortfolioDetail().getAmount())
        .averagePurchasePrice(reqCurrencyPurchaseDTO.getPortfolioDetail().getAveragePurchasePrice())
        .totalPurchasePrice(reqCurrencyPurchaseDTO.getPortfolioDetail().getTotalPurchasePrice())
        .build();

        portfolioDetailServiceApiV1.postPortfolioDetailPurchase(tempReqPostPortfolioDetailPerchaseDTO, customUserDetails);


        ReqPostTransactionDTO tempReqPostTransactionDTO = ReqPostTransactionDTO.builder()
        .assetIdx(reqCurrencyPurchaseDTO.getAsset().getIdx())
        .type(reqCurrencyPurchaseDTO.getTransaction().getType())
        .amount(reqCurrencyPurchaseDTO.getTransaction().getAmount())
        .priceAvg(reqCurrencyPurchaseDTO.getTransaction().getPriceAvg())
        .profit(reqCurrencyPurchaseDTO.getTransaction().getProfit())
        .build();

        transactionServiceApiV1.postTransaction(tempReqPostTransactionDTO, customUserDetails);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("구매 성공")
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<?> sellCurrency(ReqCurrencySellDTO reqCurrencySellDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqCurrencySellDTO.getAssetIdx());
        if(!assetEntityOptional.isPresent()){
            throw new BadRequestException("자산 정보를 찾을 수 없습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();
        
        ReqPostPortfolioDetailSellDTO reqPostPortfolioDetailSellDTO = ReqPostPortfolioDetailSellDTO.builder()
        .portfolioIdx(reqCurrencySellDTO.getPortfolioDetail().getPortfolioIdx())
        .assetIdx(reqCurrencySellDTO.getAssetIdx())
        .amount(reqCurrencySellDTO.getPortfolioDetail().getAmount())
        // .totalSellPrice(reqCurrencySellDTO.getPortfolioDetail().getTotalSellPrice())
        .build();

        portfolioDetailServiceApiV1.postPortfolioDetailSell(reqPostPortfolioDetailSellDTO, customUserDetails);

        ReqPostTransactionDTO reqPostTransactionDTO = ReqPostTransactionDTO.builder()
        .assetIdx(reqCurrencySellDTO.getAssetIdx())
        // .type(reqCurrencySellDTO)
        .build();

        return null;

    //         private String assetIdx;
    // private String type;
    // private BigDecimal amount;
    // private BigDecimal priceAvg;
    // private BigDecimal profit;

    }
}
