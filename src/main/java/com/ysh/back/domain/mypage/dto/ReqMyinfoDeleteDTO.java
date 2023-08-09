package com.ysh.back.domain.mypage.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqMyinfoDeleteDTO {
    @NotNull(message = "해당 유저가 존재하지 않습니다.")
    private Long idx;
    private LocalDateTime deletedAt;
}
