package com.ysh.back.model.asset.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ysh.back.model.bookmark.entity.BookmarkEntity;
import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;
import com.ysh.back.model.transaction.entity.TransactionEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ASSET")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
@EqualsAndHashCode(of = "idx", callSuper = false)
public class AssetEntity {

    @Id
    @Column(name = "idx", nullable = false, unique = true)
    private String idx;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_idx", nullable = false)
    private AssetTypeEntity assetTypeEntity;
    
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioDetailEntity> portfolioDetailEntityList;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkEntity> bookmarkEntityList;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionEntityList;
}
