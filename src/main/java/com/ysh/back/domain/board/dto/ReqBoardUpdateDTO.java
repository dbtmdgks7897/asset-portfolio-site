package com.ysh.back.domain.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReqBoardUpdateDTO {
    private String name;
    private String nameOrigin;
    private String content;
    private String contentOrigin;
    private LocalDateTime updatedAt;
}
