package com.ysh.back.domain.analyze.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.ysh.back.domain.asset.dto.ApiGetDomesticStockNameDTO.Response;
import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;
import com.ysh.back.model.portfolio.repository.PortfolioDetailRepository;
import com.ysh.back.model.portfolio.repository.PortfolioRepository;
import com.ysh.back.model.user.entity.UserEntity;
import com.ysh.back.model.user.repository.UserRepository;

@Service
public class AnalyzeServiceApiV1 {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PortfolioDetailRepository portfolioDetailRepository;
    @Autowired
    PortfolioRepository portfolioRepository;

    public ResponseEntity<?> getAnalyzeData(Integer portfolioIdx, CustomUserDetails customUserDetails) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(customUserDetails.getUsername());
        if (!userEntityOptional.isPresent()) {
            throw new BadRequestException("사용자 정보를 찾을 수 없습니다.");
        }

        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findByIdx(portfolioIdx);
        if (!portfolioEntityOptional.isPresent()) {
            throw new BadRequestException("포트폴리오 정보를 찾을 수 없습니다.");
        }

        List<PortfolioDetailEntity> portfolioDetailList = portfolioDetailRepository
                .findByPortfolioEntityIdxAndDeletedAtIsNull(portfolioIdx);

        Map<String, BigDecimal[]> assetSummariesMap = new HashMap<>();
        BigDecimal[] totalSummaries = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
        for (PortfolioDetailEntity detail : portfolioDetailList) {
            String assetName = detail.getAssetEntity().getAssetTypeEntity().getName();
            if (assetName.contains("주식")) {
                assetName = "주식";
            }

            if (!assetSummariesMap.containsKey(assetName)) {
                BigDecimal[] bigDecimalArray = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
                assetSummariesMap.put(assetName, bigDecimalArray);
            }

            BigDecimal curPrice;
            if (detail.getAssetEntity().getPrice() != null) {
                curPrice = detail.getAssetEntity().getPrice().multiply(detail.getAmount());
            } else {
                curPrice = detail.getTotalPurchasePrice();
            }

            BigDecimal[] summaries = assetSummariesMap.get(assetName);
            BigDecimal totalPurchase = summaries[0].add(detail.getTotalPurchasePrice());
            BigDecimal totalPrice = summaries[1].add(curPrice);
            BigDecimal totalProfit = totalPrice.subtract(totalPurchase);
            BigDecimal totalProfitRate = totalProfit.divide(totalPurchase, 2, RoundingMode.HALF_UP);
            
            summaries[0] = totalPurchase;
            summaries[1] = totalPrice;
            summaries[2] = totalProfit;
            summaries[3] = totalProfitRate;

            assetSummariesMap.put(assetName, summaries);

            totalSummaries[0] = totalSummaries[0].add(detail.getTotalPurchasePrice());
            totalSummaries[1] = totalSummaries[1].add(curPrice);
            totalSummaries[2] = totalSummaries[2].add(totalProfit);
            totalSummaries[3] = totalSummaries[3].add(totalProfitRate);
        }

        assetSummariesMap.put("총", totalSummaries);

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(0)
                        .message("분석 정보 가져오기 성공")
                        .data(assetSummariesMap)
                        .build(),
                HttpStatus.OK);
    }
}
