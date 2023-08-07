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
    private List<Comment> commentList;

    public static ResBoardDetailsDTO of(BoardEntity boardEntity) {
        return ResBoardDetailsDTO.builder()
                .idx(boardEntity.getIdx())
                .name(boardEntity.getName())
                .createdAt(boardEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd hh:mm"))))
                .user(User.fromEntity(boardEntity.getUserEntity()))
                .viewCount(boardEntity.getViewCount())
                .recommendCount(boardEntity.getRecommendCount())
                .content(boardEntity.getContent())
                .commentList(
                        boardEntity.getCommentEntityList()
                                .stream()
                                .map(commentEntity -> Comment.fromEntity(commentEntity))
                                .toList())
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Comment {
        private Long idx;
        private String createdAt;
        private String content;
        private Integer recommendCount;
        private User user;

        public static Comment fromEntity(CommentEntity commentEntity) {
            return Comment.builder()
                    .idx(commentEntity.getIdx())
                    .createdAt(commentEntity.getCreatedAt().format(DateTimeFormatter.ofPattern(("yy/MM/dd hh:mm"))))
                    .content(commentEntity.getContent())
                    .recommendCount(commentEntity.getRecommendCount())
                    .user(User.fromEntity(commentEntity.getUserEntity()))
                    .build();
        };

        @AllArgsConstructor
        @NoArgsConstructor
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
}
