package com.ysh.back.domain.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqPortfolioInsertDTO {

    @NotBlank(message = "포트폴리오 이름을 작성해주세요")
    private String name;

}
