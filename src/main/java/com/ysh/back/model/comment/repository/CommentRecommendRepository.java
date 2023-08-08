package com.ysh.back.model.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.comment.entity.CommentRecommendEntity;
import com.ysh.back.model.comment.entity.CommentRecommendEntityKey;

@Repository
public interface CommentRecommendRepository extends JpaRepository<CommentRecommendEntity, CommentRecommendEntityKey> {
    Optional<CommentRecommendEntity> findByCommentIdxAndUserIdx(Long boardIdx, Long userIdx);
}
