package com.ysh.back.model.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.board.entity.BoardRecommendEntity;
import com.ysh.back.model.board.entity.BoardRecommendEntityKey;

@Repository
public interface BoardRecommendRepository extends JpaRepository<BoardRecommendEntity, BoardRecommendEntityKey> {
    Optional<BoardRecommendEntity> findByBoardIdxAndUserIdx(Long boardIdx, Long userIdx);
}
