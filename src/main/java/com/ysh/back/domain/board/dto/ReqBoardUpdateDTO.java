package com.ysh.back.domain.board.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReqBoardUpdateDTO {
    @NotBlank(message = "게시물 제목을 입력하세요.")
    private String name;
    private String nameOrigin;
    @NotBlank(message = "게시물 내용을 입력하세요.")
    private String content;
    private String contentOrigin;
    private LocalDateTime updatedAt;
}
