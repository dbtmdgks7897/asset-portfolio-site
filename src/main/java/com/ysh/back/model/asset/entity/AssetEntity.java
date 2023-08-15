package com.ysh.back.model.asset.entity;

import java.util.List;

import com.ysh.back.model.bookmark.entity.BookmarkEntity;
import com.ysh.back.model.portfolio.entity.PortfolioDetailEntity;
import com.ysh.back.model.transaction.entity.TransactionEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioDetailEntity> portfolioDetailEntityList;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkEntity> bookmarkEntityList;

    @OneToMany(mappedBy = "assetEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionEntityList;
}
