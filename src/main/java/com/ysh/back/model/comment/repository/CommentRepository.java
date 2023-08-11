package com.ysh.back.model.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ysh.back.domain.comment.dto.ResBoardCommentListDTO.Comment;
import com.ysh.back.model.comment.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    Optional<CommentEntity> findByIdx(Long commentIdx);

    List<CommentEntity> findByBoardEntityIdx(Long boardIdx);
    List<CommentEntity> findByBoardEntityIdxAndDeletedAtNullOrderByCreatedAtDesc(Long boardIdx);

     @Modifying
    @Query("UPDATE CommentEntity b SET b.recommendCount = b.recommendCount + 1 WHERE b.idx = :commentIdx")
    void incrementRecommendCount(@Param("commentIdx") Long commentIdx);

    List<CommentEntity> findAllByUserEntity_IdxOrderByCreatedAtDesc(Long userIdx);
}
