package com.ysh.back.model.bookmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.bookmark.entity.BookmarkEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer>{
    Optional<BookmarkEntity> findByUserEntityIdxAndAssetEntityIdx(Long userEntityIdx, String assetEntityIdx);
}
