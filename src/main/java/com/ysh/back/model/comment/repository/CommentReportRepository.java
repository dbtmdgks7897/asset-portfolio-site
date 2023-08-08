package com.ysh.back.model.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.comment.entity.CommentReportEntity;
import com.ysh.back.model.comment.entity.CommentReportEntityKey;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReportEntity, CommentReportEntityKey> {
    Optional<CommentReportEntity> findByCommentIdxAndUserIdx(Long boardIdx, Long userIdx);
}
