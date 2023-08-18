package com.ysh.back.domain.portfolio.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.ysh.back.domain.portfolio.dto.chart.ChartDataDTO;
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
public class PortfolioServiceApiV1 {

    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PortfolioDetailRepository portfolioDetailRepository;

    @Transactional
    public ResponseEntity<?> getPortfolioListData(CustomUserDetails customUserDetails) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보가 존재하지 않습니다.");
        }
        UserEntity userEntity = userEntityOptional.get();

        List<PortfolioEntity> portfolioEntityList = portfolioRepository.findByUserEntityIdx(userEntity.getIdx());
        if (portfolioEntityList.isEmpty()) {
            return null;
        }

        ResPortfolioListDTO dto = ResPortfolioListDTO.of(portfolioEntityList);

        int count = 0;
        for (PortfolioEntity portfolioEntity : portfolioEntityList) {
            Integer portfolioIdx = portfolioEntity.getIdx();
            List<String> assetTypes = new ArrayList<>();
            List<BigDecimal> assetSums = new ArrayList<>();

            List<PortfolioDetailEntity> portfolioDetailList = portfolioDetailRepository
                    .findByPortfolioEntityIdx(portfolioIdx);

            // 포트폴리오의 각 자산을 순회하면서 자산 타입과 금액을 리스트에 추가하고 더해갑니다.
            for (PortfolioDetailEntity portfolioDetailEntity : portfolioDetailList) {
                String assetType = portfolioDetailEntity.getAssetEntity().getAssetTypeEntity().getName();
                BigDecimal assetAmount = portfolioDetailEntity.getTotalPurchasePrice();

                int index = assetTypes.indexOf(assetType);

                if (index != -1) {
                    // 이미 해당 자산 타입이 리스트에 존재하는 경우 합을 업데이트합니다.
                    BigDecimal currentAmount = assetSums.get(index);
                    assetSums.set(index, currentAmount.add(assetAmount));
                } else {
                    // 해당 자산 타입이 리스트에 없는 경우 추가합니다.
                    assetTypes.add(assetType);
                    assetSums.add(assetAmount);
                }
            }
            ChartDataDTO chartData = ChartDataDTO.of(assetTypes, assetSums);

            dto.getPortfolioList().get(count++).setChartDataDTO(chartData);
            // 현재 포트폴리오에 해당하는 assetTypes와 assetSums를 이용하여 필요한 작업을 수행합니다.
        }

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
