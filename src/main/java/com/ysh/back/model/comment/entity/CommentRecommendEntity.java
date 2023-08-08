package com.ysh.back.model.comment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMMENT_RECOMMEND")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@IdClass(CommentRecommendEntityKey.class)
public class CommentRecommendEntity implements Serializable {
    @Id
    @Column(name = "comment_idx", nullable = false, unique = true)
    private Long commentIdx;

    @Id
    @Column(name = "user_idx", nullable = false, unique = true)
    private Long userIdx;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
