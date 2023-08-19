package com.ysh.back.domain.portfolio.detail.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqDeletePortfolioDetailDTO {
    @NotNull(message = "코드를 정확히 입력해주세요")
    private String code;
}
