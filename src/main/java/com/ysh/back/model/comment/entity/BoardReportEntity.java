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
@Table(name = "BOARD_REPORT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@IdClass(CommentReportEntityKey.class)
public class BoardReportEntity implements Serializable{
    @Id
    @Column(name = "comment_idx", nullable = false, unique = true)
    private Long commentIdx;

    @Id
    @Column(name = "user_idx", nullable = false, unique = true)
    private Long userIdx;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
