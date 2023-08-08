package com.ysh.back.domain.board.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.user.entity.UserEntity;

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

    public static ResBoardListDTO of(List<BoardEntity> boardEntityList){
        return ResBoardListDTO.builder()
        .boardList(
            boardEntityList.stream()
            .map(boardEntity -> Board.fromEntity(boardEntity))
            .toList()
        )
        .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class Board{
        private Long idx;
        private String name;
        private User user;
        private String createdAt;
        private Integer viewCount;
        private Integer recommendCount;

        public static Board fromEntity(BoardEntity boardEntity){
            return Board.builder()
            .idx(boardEntity.getIdx())
            .name(boardEntity.getName())
            .user(User.fromEntity(boardEntity.getUserEntity()))
            .createdAt(boardEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd hh:mm"))))
            .viewCount(boardEntity.getViewCount())
            .recommendCount(boardEntity.getRecommendCount())
            .build();
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Builder
        private static class User{

            private String name;

            public static User fromEntity(UserEntity userEntity){
                return User.builder()
                .name(userEntity.getName())
                .build();
            }
        }
    }
}
