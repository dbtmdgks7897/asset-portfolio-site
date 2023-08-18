package com.ysh.back.domain.asset.bitcoin.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.asset.bitcoin.dto.ResGetBitcoinDetailDTO;
import com.ysh.back.domain.asset.dto.ReqPostAssetDTO;
import com.ysh.back.domain.asset.service.AssetServiceApiV1;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.transaction.repository.TransactionRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BitcoinServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    AssetServiceApiV1 assetServiceApiV1;

    private final RestTemplate restTemplate;

    @Autowired
    public BitcoinServiceApiV1(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Transactional
    public ResponseEntity<?> getBitcoinDetail(String bitcoinCode, String bitcoinName,
            CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        String url = "https://api.upbit.com/v1/ticker?markets=" + bitcoinCode;

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");

        ResponseEntity<ResGetBitcoinDetailDTO[]> response = restTemplate.getForEntity(url, ResGetBitcoinDetailDTO[].class);
        ResGetBitcoinDetailDTO responseBody = response.getBody()[0];

        if(responseBody == null){
            throw new BadRequestException("해당 종목이 존재하지 않습니다.");
        }

        ReqPostAssetDTO reqPostAssetDTO = ReqPostAssetDTO.builder()
                .assetCode(bitcoinCode)
                .assetName(bitcoinName)
                .assetType("암호화폐")
                .build();
        assetServiceApiV1.postAssetData(reqPostAssetDTO, customUserDetails);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .data(response.getBody())
                        .build(),
                HttpStatus.OK);
    }

    public BigDecimal updatePrice(String bitcoinCode){
        String url = "https://api.upbit.com/v1/ticker?markets=" + bitcoinCode;

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");

        ResponseEntity<ResGetBitcoinDetailDTO[]> response = restTemplate.getForEntity(url, ResGetBitcoinDetailDTO[].class);
        ResGetBitcoinDetailDTO responseBody = response.getBody()[0];

        BigDecimal price = responseBody.getTrade_price();

        return price;
    }
}
