package com.ysh.back.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReqBoardRecommendDTO {
    private Long userIdx;
    private Long boardIdx;
}
