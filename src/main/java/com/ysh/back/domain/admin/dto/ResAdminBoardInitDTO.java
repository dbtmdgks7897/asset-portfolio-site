package com.ysh.back.domain.admin.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResAdminBoardInitDTO {
    
    List<Board> boardList;

    public static ResAdminBoardInitDTO of(List<BoardEntity> boardEntityList){
        return ResAdminBoardInitDTO.builder()
        .boardList(
            boardEntityList.stream()
            .map(boardEntity -> Board.fromEntity(boardEntity))
            .toList()
        )
        .build();
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    private static class Board{
        private Long idx;
        private String name;
        private User user;
        private String createdAt;
        private Integer viewCount;
        private Integer recommendCount;
        private Boolean isHide;
        private String deletedAt;

        public static Board fromEntity(BoardEntity boardEntity){
            return Board.builder()
            .idx(boardEntity.getIdx())
            .name(boardEntity.getName())
            .user(User.fromEntity(boardEntity.getUserEntity()))
            .createdAt(boardEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd"))))
            .viewCount(boardEntity.getViewCount())
            .recommendCount(boardEntity.getRecommendCount())
            .isHide(boardEntity.getIsHided())
            .deletedAt(boardEntity.getDeletedAt() != null ? boardEntity.getDeletedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd"))) : null)
            .build();
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        private static class User{
            private Long idx;
            private String email;

            public static User fromEntity(UserEntity userEntity){
                return User.builder()
                .idx(userEntity.getIdx())
                .email(userEntity.getEmail())
                .build();
            }
        }
    }
}
