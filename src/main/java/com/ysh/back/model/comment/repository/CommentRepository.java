package com.ysh.back.model.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.comment.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findByBoardEntityIdx(Long boardIdx);
    List<CommentEntity> findByBoardEntityIdxOrderByCreatedAtAsc(Long boardIdx);

     @Modifying
    @Query("UPDATE CommentEntity b SET b.recommendCount = b.recommendCount + 1 WHERE b.idx = :commentIdx")
    void incrementRecommendCount(@Param("commentIdx") Long commentIdx);

    List<CommentEntity> findAllByUserEntity_Idx(Long userIdx);
}
