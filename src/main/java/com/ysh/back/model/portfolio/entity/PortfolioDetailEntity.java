package com.ysh.back.model.portfolio.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PORTFOLIO_DETAIL")
public class PortfolioDetailEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, unique = true)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "portfolio_idx", referencedColumnName = "idx", updatable = false, nullable = false)
    private PortfolioEntity portfolioEntity;

    @ManyToOne
    @JoinColumn(name = "asset_idx", referencedColumnName = "idx", updatable = false, nullable = false)
    private AssetEntity assetEntity;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "average_purchase_price")
    private BigDecimal averagePurchasePrice;

    @Column(name = "total_purchase_price")
    private BigDecimal totalPurchasePrice;

    @Column(name = "dividend_month")
    private BigDecimal dividendMonth;

    @Column(name = "dividend_amount")
    private BigDecimal dividendAmount;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
