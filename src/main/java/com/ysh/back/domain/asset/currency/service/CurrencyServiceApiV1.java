package com.ysh.back.domain.asset.currency.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.currency.dto.ReqCurrencyPurchaseDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;
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

    public ResponseEntity<?> purchaseCurrency(ReqCurrencyPurchaseDTO reqCurrencyPurchaseDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        ReqPostAssetDTO reqPostAssetDTO = ReqPostAssetDTO.builder()
        .assetCode(reqCurrencyPurchaseDTO.getAsset().getIdx())
        .assetName(reqCurrencyPurchaseDTO.getAsset().getName())
        .assetType(reqCurrencyPurchaseDTO.getAsset().getType())
        .build();

        assetServiceApiV1.postAssetData(reqPostAssetDTO, customUserDetails);
        
        
    }

    public ResponseEntity<?> sellCurrency(){
        
    }
}
