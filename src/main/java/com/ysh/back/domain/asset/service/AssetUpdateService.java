package com.ysh.back.domain.asset.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ysh.back.domain.asset.bitcoin.service.BitcoinServiceApiV1;
import com.ysh.back.domain.asset.currency.service.CurrencyServiceApiV1;
import com.ysh.back.domain.asset.stock.domestic.service.DomesticStockServiceApiV1;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetUpdateService {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    DomesticStockServiceApiV1 domesticStockServiceApiV1;

    @Autowired
    BitcoinServiceApiV1 bitcoinServiceApiV1;

    @Autowired
    CurrencyServiceApiV1 currencyServiceApiV1;
    
    @Transactional
    // @Scheduled(initialDelay = 5000, fixedRate = 5000)
    @Scheduled(fixedRate = 3600000 * 24)
    public void updateAllAsset(){
        List<AssetEntity> allAsset = assetRepository.findAll();
        allAsset.forEach((asset) -> {
            System.out.println(asset.getAssetTypeEntity().getName());
            if(asset.getAssetTypeEntity().getName().equals("주식_국내")){
                BigDecimal price = domesticStockServiceApiV1.updatePrice(asset.getIdx());
                asset.setPrice(price);
                asset.setUpdatedAt(LocalDateTime.now());
            }else if(asset.getAssetTypeEntity().getName().equals("암호화폐")){
                BigDecimal price = bitcoinServiceApiV1.updatePrice(asset.getIdx());
                asset.setPrice(price);
                asset.setUpdatedAt(LocalDateTime.now());
            }else if(asset.getAssetTypeEntity().getName().equals("외화")){
                BigDecimal price = currencyServiceApiV1.updatePrice(asset.getIdx());
                asset.setPrice(price);
                asset.setUpdatedAt(LocalDateTime.now());
            }
        });
        System.out.println("업데이트 실행");
    }
}
