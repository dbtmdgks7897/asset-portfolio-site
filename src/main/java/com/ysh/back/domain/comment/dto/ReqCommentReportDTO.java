package com.ysh.back.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReqCommentReportDTO {
    @NotBlank(message = "사유를 입력해주세요.")
    private String reason;
}
