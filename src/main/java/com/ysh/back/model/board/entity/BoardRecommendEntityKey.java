package com.ysh.back.model.board.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRecommendEntityKey {
    private Long boardIdx;
    private Long userIdx;
}
