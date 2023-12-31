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
public class ReqAdminUserSuspendData {
    @NotBlank(message = "정지 기간 입력 필요")
    private Integer suspendDuration;
    @NotBlank(message = "정지 사유 입력 필요")
    private String suspendReason;
}
