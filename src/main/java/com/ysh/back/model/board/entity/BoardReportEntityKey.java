package com.ysh.back.model.board.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportEntityKey {
    private Long boardIdx;
    private Long userIdx;
}
