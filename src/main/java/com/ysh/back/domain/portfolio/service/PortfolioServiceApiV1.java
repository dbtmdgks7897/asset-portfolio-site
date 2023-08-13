package com.ysh.back.domain.portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.common.exception.ConflictException;
import com.ysh.back.config.security.auth.CustomUserDetails;
import com.ysh.back.domain.portfolio.dto.ReqPortfolioInsertDTO;
import com.ysh.back.domain.portfolio.dto.ResPortfolioListDTO;
import com.ysh.back.model.auditLog.entity.AuditLogEntity;
import com.ysh.back.model.auditLog.repository.AuditLogRepository;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;
import com.ysh.back.model.portfolio.repository.PortfolioRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PortfolioServiceApiV1 {

    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> getPortfolioListData(CustomUserDetails customUserDetails) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        List<PortfolioEntity> portfolioEntitiyList = portfolioRepository.findByUserEntityIdx(userEntity.getIdx());
        if(portfolioEntitiyList.isEmpty()){
            return null;
        }

        ResPortfolioListDTO dto = ResPortfolioListDTO.of(portfolioEntitiyList);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("포트폴리오 리스트 가져오기 성공")
                        .data(dto)
                        .build(),
                HttpStatus.OK);
        
    }


    @Transactional
    public ResponseEntity<?> insertPortfolioData(ReqPortfolioInsertDTO reqPortfolioInsertDTO,
            CustomUserDetails customUserDetails) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository
                .findByName(reqPortfolioInsertDTO.getName());

        if (portfolioEntityOptional.isPresent()) {
            throw new ConflictException("이미 사용중인 이름입니다.");
        }

        PortfolioEntity portfolioEntity = PortfolioEntity.builder()
                .userEntity(userEntity)
                .name(reqPortfolioInsertDTO.getName())
                .description(reqPortfolioInsertDTO.getDescription())
                .build();

        portfolioRepository.save(portfolioEntity);

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .tableName("portfolio")
                // 유저 정보 가져올 수 있을 때 바꾸자.
                .userIdx(userEntity.getIdx())
                .rowId(Long.valueOf(portfolioEntity.getIdx()))
                .operation("INSERT")
                .oldValue("")
                .newValue(portfolioEntity.getName())
                .reason("포트폴리오 생성")
                .build();

        auditLogRepository.save(auditLog);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("새로운 포트폴리오가 생성되었습니다.")
                        .build(),
                HttpStatus.OK);
    }

}
