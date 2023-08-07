package com.ysh.back.domain.board.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ysh.back.model.board.entity.BoardEntity;
import com.ysh.back.model.comment.entity.CommentEntity;
import com.ysh.back.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResBoardDetailsDTO {

    private Long idx;
    private String name;
    private String createdAt;
    private User user;
    private Integer viewCount;
    private Integer recommendCount;
    private String content;

    public static ResBoardDetailsDTO fromEntity(BoardEntity boardEntity) {
        return ResBoardDetailsDTO.builder()
                .idx(boardEntity.getIdx())
                .name(boardEntity.getName())
                .createdAt(boardEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd hh:mm"))))
                .user(User.fromEntity(boardEntity.getUserEntity()))
                .viewCount(boardEntity.getViewCount())
                .recommendCount(boardEntity.getRecommendCount())
                .content(boardEntity.getContent())
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class User {
        private Long idx;
        private String name;

        public static User fromEntity(UserEntity userEntity) {
            return User.builder()
                    .idx(userEntity.getIdx())
                    .name(userEntity.getName())
                    .build();
        }
    }
}
