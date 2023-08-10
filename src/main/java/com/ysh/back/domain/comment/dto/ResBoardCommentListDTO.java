package com.ysh.back.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ysh.back.model.comment.entity.CommentEntity;
import com.ysh.back.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ResBoardCommentListDTO {

    List<Comment> commentList;

    public static ResBoardCommentListDTO of(List<CommentEntity> commentEntityList) {
        return ResBoardCommentListDTO.builder()
                .commentList(
                        commentEntityList.stream()
                                .map(commentEntity -> Comment.fromEntity(commentEntity))
                                .toList())
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Comment {
        private Long idx;
        private LocalDateTime createdAt;
        private String content;
        private Integer recommendCount;
        private User user;

        public static Comment fromEntity(CommentEntity commentEntity) {
            return Comment.builder()
                    .idx(commentEntity.getIdx())
                    .createdAt(commentEntity.getCreatedAt())
                    .content(commentEntity.getContent())
                    .recommendCount(commentEntity.getRecommendCount())
                    .user(User.fromEntity(commentEntity.getUserEntity()))
                    .build();
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Builder
        public static class User {
            private Long idx;
            private String nickname;

            public static User fromEntity(UserEntity userEntity) {
                return User.builder()
                        .idx(userEntity.getIdx())
                        .nickname(userEntity.getNickname())
                        .build();
            }
        }
    }

}
