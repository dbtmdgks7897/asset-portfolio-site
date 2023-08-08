package com.ysh.back.model.board.entity;

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
@IdClass(BoardReportEntityKey.class)
public class BoardReportEntity implements Serializable{
    @Id
    @Column(name = "board_idx", nullable = false, unique = true)
    private Long boardIdx;

    @Id
    @Column(name = "user_idx", nullable = false, unique = true)
    private Long userIdx;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
