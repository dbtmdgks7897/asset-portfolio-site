package com.ysh.back.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqAdminUserSuspendData {
    private Integer suspendDuration;
    private String suspendReason;
}
