package com.ysh.back.domain.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.transaction.dto.ReqPostTransactionDTO;
import com.ysh.back.domain.transaction.dto.ResTransactionListDTO;
import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.repository.AssetRepository;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;
import com.ysh.back.model.portfolio.repository.PortfolioRepository;
import com.ysh.back.model.transaction.entity.TransactionEntity;
import com.ysh.back.model.transaction.repository.TransactionRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class TransactionServiceApiV1 {
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    
    public ResponseEntity<?> postTransaction(ReqPostTransactionDTO reqPostTransactionDTO, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<AssetEntity> assetEntityOptional = assetRepository.findByIdx(reqPostTransactionDTO.getAssetIdx());
        if(!assetEntityOptional.isPresent()){
            throw new BadRequestException("해당 자산 정보가 존재하지 않습니다.");
        }
        AssetEntity assetEntity = assetEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(reqPostTransactionDTO.getPortfolioIdx());
        if(!portfolioEntityOptional.isPresent()){
            throw new BadRequestException("해당 포트폴리오 정보가 존재하지 않습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();

        TransactionEntity transactionEntityForSaving = TransactionEntity.builder()
        .userEntity(userEntity)
        .portfolioEntity(portfolioEntity)
        .assetEntity(assetEntity)
        .type(reqPostTransactionDTO.getType())
        .amount(reqPostTransactionDTO.getAmount())
        .priceAvg(reqPostTransactionDTO.getPriceAvg())
        .profit(reqPostTransactionDTO.getProfit())
        .build();

        TransactionEntity transactionEntity = transactionRepository.save(transactionEntityForSaving);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("transaction")
                .userIdx(userEntity.getIdx())
                .rowId(Long.valueOf(transactionEntity.getIdx()))
                .operation("INSERT")
                .newValue(transactionEntity.getAssetEntity().getName())
                .reason(transactionEntity.getType())
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("새 거래(내역)가 생성되었습니다.")
                        .build(),
                HttpStatus.OK);
    }
    
    public ResponseEntity<?> getAllTransactionList(Integer portfolioIdx, CustomUserDetails customUserDetails){
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername()); 
        if(!userEntityOptional.isPresent()){
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();
        
        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(portfolioIdx);
        if(!portfolioEntityOptional.isPresent()){
            throw new BadRequestException("해당 포트폴리오 정보를 찾을 수 없습니다.");
        }
        PortfolioEntity portfolioEntity = portfolioEntityOptional.get();
        
        if(userEntity.getIdx() != portfolioEntity.getUserEntity().getIdx()){
            throw new BadRequestException("해당 포트폴리오 접근 권한이 없습니다.");
        }

        List<TransactionEntity> transactionEntityList = transactionRepository.findByPortfolioEntityIdx(portfolioIdx);
        if(transactionEntityList.isEmpty()){
            return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("거래 내역 없음!")
                        .build(),
                HttpStatus.OK);
        }
        
        ResTransactionListDTO dto = ResTransactionListDTO.of(transactionEntityList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("거래 내역 조회 성공")
                        .data(dto)
                        .build(),
                HttpStatus.OK);
    }
}
