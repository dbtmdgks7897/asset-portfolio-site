package com.ysh.back.model.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.comment.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findByBoardEntityIdx(Long boardIdx);
    List<CommentEntity> findByBoardEntityIdxOrderByCreatedAtAsc(Long boardIdx);
}
