package com.ysh.back.domain.asset.dto;

import java.util.List;

import com.ysh.back.model.asset.entity.AssetTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResGetAssetTypeListDTO {
    
    private List<AssetType> assetTypeList;

    public static ResGetAssetTypeListDTO of(List<AssetTypeEntity> entityList){
        return ResGetAssetTypeListDTO.builder()
        .assetTypeList(
            entityList.stream()
            .map(entity -> AssetType.fromEntity(entity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class AssetType{
        private Integer idx;
        private String name;

        public static AssetType fromEntity(AssetTypeEntity assetTypeEntity){
            return AssetType.builder()
            .idx(assetTypeEntity.getIdx())
            .name(assetTypeEntity.getName())
            .build();
        }
    }
}
