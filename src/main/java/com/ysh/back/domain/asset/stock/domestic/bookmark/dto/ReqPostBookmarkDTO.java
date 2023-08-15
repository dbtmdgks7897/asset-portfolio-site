package com.ysh.back.domain.asset.stock.domestic.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqPostBookmarkDTO {
    private String stockIdx;
}
