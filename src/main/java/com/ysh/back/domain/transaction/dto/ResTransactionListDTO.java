package com.ysh.back.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.transaction.entity.TransactionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResTransactionListDTO {
    List<Transaction> transactionList;

    public static ResTransactionListDTO of(List<TransactionEntity> entityList){
        return ResTransactionListDTO.builder()
        .transactionList(
            entityList.stream()
            .map(entity -> Transaction.fromEntity(entity))
            .toList()
        )
        .build();
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Transaction {
        private Integer idx;
        private String type;
        private String transactionDate;
        private String assetName;
        private BigDecimal amount;
        private BigDecimal priceAvg;
        private BigDecimal profit;

    public static Transaction fromEntity(TransactionEntity entity){
        return Transaction.builder()
        .idx(entity.getIdx())
        .type(entity.getType())
        .transactionDate(entity.getTransactionDate().format(DateTimeFormatter.ofPattern(("yy/MM/dd"))))
        .assetName(entity.getAssetEntity().getName())
        .amount(entity.getAmount())
        .priceAvg(entity.getPriceAvg())
        .profit(entity.getProfit())
        .build();
    }
    }

}
