package com.ysh.back.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReqBoardPostDTO {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "게시물 내용을 입력해주세요")
    private String content;
}
