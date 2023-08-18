package com.ysh.back.model.portfolio.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ysh.back.model.asset.entity.AssetEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PORTFOLIO_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
    private String dividendMonth;

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
