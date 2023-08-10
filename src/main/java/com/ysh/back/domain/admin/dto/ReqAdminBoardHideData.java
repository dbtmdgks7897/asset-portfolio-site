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
public class ReqAdminBoardHideData {
    @NotBlank(message = "숨김 사유 입력 필요")
    private String hideReason;
}
