package com.ysh.back.model.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.board.entity.BoardEntity;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
    
    List<BoardEntity> findByNameContainingOrContentContainingOrUserEntity_NameContaining(String nameSearch, String contentSearch, String userSearch);
}
