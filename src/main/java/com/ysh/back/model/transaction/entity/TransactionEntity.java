package com.ysh.back.model.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.portfolio.entity.PortfolioEntity;
import com.ysh.back.model.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRANSACTION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, unique = true)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx", referencedColumnName = "idx", updatable = false, nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "asset_idx", referencedColumnName = "idx", updatable = false, nullable = false)
    private AssetEntity assetEntity;
    
    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "profit")
    private BigDecimal profit;

    @Column(name = "transaction_date")
    @CreationTimestamp
    private LocalDateTime transactionDate;
}
