package com.ysh.back.model.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.board.entity.BoardReportEntity;
import com.ysh.back.model.board.entity.BoardReportEntityKey;

@Repository
public interface BoardReportRepository extends JpaRepository<BoardReportEntity, BoardReportEntityKey> {
    Optional<BoardReportEntity> findByBoardIdxAndUserIdx(Long boardIdx, Long userIdx);
}
