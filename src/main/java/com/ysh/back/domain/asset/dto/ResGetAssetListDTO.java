package com.ysh.back.domain.asset.dto;

import java.util.List;

import com.ysh.back.model.asset.entity.AssetEntity;
import com.ysh.back.model.asset.entity.AssetTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResGetAssetListDTO {
    
    private List<Asset> assetList;

    public static ResGetAssetListDTO of(List<AssetEntity> entityList){
        return ResGetAssetListDTO.builder()
        .assetList(
            entityList.stream()
            .map(entity -> Asset.fromEntity(entity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class Asset{
        private String assetCode;
        private String assetName;
        private AssetType assetType;

        public static Asset fromEntity(AssetEntity assetEntity){
            return Asset.builder()
            .assetCode(assetEntity.getIdx())
            .assetName(assetEntity.getName())
            .assetType(AssetType.fromEntity(assetEntity.getAssetTypeEntity()))
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
}
