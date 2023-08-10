package com.ysh.back.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqAdminUserDeletedDataDTO {
    @NotBlank(message = "사유를 입력해주세요.")
    private String reason;
}
