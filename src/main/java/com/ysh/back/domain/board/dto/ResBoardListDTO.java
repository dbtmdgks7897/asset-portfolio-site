package com.ysh.back.domain.board.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResBoardListDTO {
    List<Board> boardList;



    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class Board{
        private Long idx;


    }
}
