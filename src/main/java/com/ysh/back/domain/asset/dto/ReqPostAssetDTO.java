package com.ysh.back.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqPostAssetDTO {
    private String assetCode;
    private String assetName;
    private String assetType;
}
