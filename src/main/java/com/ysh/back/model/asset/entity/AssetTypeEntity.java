package com.ysh.back.model.asset.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssetTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "assetTypeEntity", fetch = FetchType.LAZY)
    private List<AssetEntity> assetEntityList;
}

