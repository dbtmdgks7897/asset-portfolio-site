package com.ysh.back.domain.mypage.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.comment.entity.CommentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResMyactiveDTO {

    private List<Board> boardList;
    private List<Comment> commentList;

    public static ResMyactiveDTO of(List<BoardEntity> boardEntityList, List<CommentEntity> commentEntityList){
        return ResMyactiveDTO.builder()
        .boardList(
            boardEntityList.stream()
            .map(boardEntity -> Board.fromEntity(boardEntity))
            .toList()
        )
        .commentList(
            commentEntityList.stream()
            .map(commentEntity -> Comment.fromEntity(commentEntity))
            .toList()
        )
        .build();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    private static class Board {
        private Long idx;
        private String name;
        private String createdAt;
        private Integer viewCount;
        private Integer recommendCount;

        public static Board fromEntity(BoardEntity boardEntity){
            return Board.builder()
            .idx(boardEntity.getIdx())
            .name(boardEntity.getName())
            .createdAt(boardEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd"))))
            .viewCount(boardEntity.getViewCount())
            .recommendCount(boardEntity.getRecommendCount())
            .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    private static class Comment {
        private Long idx;
        private String content;
        private Board board;
        private String createdAt;
        private Integer recommendCount;

        public static Comment fromEntity(CommentEntity commentEntity){
            return Comment.builder()
            .idx(commentEntity.getIdx())
            .content(commentEntity.getContent())
            .board(Board.fromEntity(commentEntity.getBoardEntity()))
            .recommendCount(commentEntity.getRecommendCount())
            .build();
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Builder
        private static class Board {
            private Long idx;
            private String name;

            public static Board fromEntity(BoardEntity boardEntity) {
                return Board.builder()
                .idx(boardEntity.getIdx())
                .name(boardEntity.getName())
                .build();
            }
        }
    }
}
