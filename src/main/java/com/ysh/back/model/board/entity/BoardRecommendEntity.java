package com.ysh.back.model.board.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

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
@Table(name = "BOARD_RECOMMEND")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@IdClass(BoardRecommendEntityKey.class)
@DynamicInsert
public class BoardRecommendEntity implements Serializable {
    @Id
    @Column(name = "board_idx", nullable = false, unique = true)
    private Long boardIdx;

    @Id
    @Column(name = "user_idx", nullable = false, unique = true)
    private Long userIdx;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
