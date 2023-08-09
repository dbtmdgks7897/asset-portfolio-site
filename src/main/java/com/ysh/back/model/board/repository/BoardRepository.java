package com.ysh.back.model.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ysh.back.model.board.entity.BoardEntity;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
       Optional<BoardEntity> findByIdx(Long boardIdx);

    List<BoardEntity> findByNameContainingOrContentContainingOrUserEntity_NameContaining(String nameSearch, String contentSearch, String userSearch);

    @Query("SELECT board FROM BoardEntity board ORDER BY "
           + "CASE WHEN :sort = 'idx' THEN LPAD(CAST(board.idx AS STRING), 10, '0') "
           + "     WHEN :sort = 'created_at' THEN board.createdAt "
           + "     WHEN :sort = 'view_count' THEN LPAD(CAST(board.viewCount AS STRING), 10, '0') "
           + "     WHEN :sort = 'recommend_count' THEN LPAD(CAST(board.recommendCount AS STRING), 10, '0') "
           + "END DESC") // DESC 키워드를 추가하여 내림차순 정렬
    List<BoardEntity> findAllOrderBySortDesc(@Param("sort") String sort);
    @Query("SELECT board FROM BoardEntity board ORDER BY "
           + "CASE WHEN :sort = 'idx' THEN LPAD(CAST(board.idx AS STRING), 10, '0') "
           + "     WHEN :sort = 'created_at' THEN board.createdAt "
           + "     WHEN :sort = 'view_count' THEN LPAD(CAST(board.viewCount AS STRING), 10, '0') "
           + "     WHEN :sort = 'recommend_count' THEN LPAD(CAST(board.recommendCount AS STRING), 10, '0') "
           + "END")
    List<BoardEntity> findAllOrderBySort(@Param("sort") String sort);


    @Query("SELECT board FROM BoardEntity board JOIN FETCH board.userEntity u "
           + "WHERE board.name LIKE CONCAT('%', :nameSearch, '%') "
           + "OR board.content LIKE CONCAT('%', :contentSearch, '%') "
           + "OR u.name LIKE CONCAT('%', :userSearch, '%') "
           + "ORDER BY "
           + "CASE WHEN :sort = 'idx' THEN LPAD(CAST(board.idx AS STRING), 10, '0') "
           + "     WHEN :sort = 'created_at' THEN board.createdAt "
           + "     WHEN :sort = 'view_count' THEN LPAD(CAST(board.viewCount AS STRING), 10, '0') "
           + "     WHEN :sort = 'recommend_count' THEN LPAD(CAST(board.recommendCount AS STRING), 10, '0') "
           + "END DESC") // 내림차순 정렬을 선택하는 변수를 추가하여 처리
    List<BoardEntity> findAllSearchedAndOrderBySortDesc(
            @Param("nameSearch") String nameSearch,
            @Param("contentSearch") String contentSearch,
            @Param("userSearch") String userSearch,
            @Param("sort") String sort
    );
    @Query("SELECT board FROM BoardEntity board JOIN FETCH board.userEntity u "
           + "WHERE board.name LIKE CONCAT('%', :nameSearch, '%') "
           + "OR board.content LIKE CONCAT('%', :contentSearch, '%') "
           + "OR u.name LIKE CONCAT('%', :userSearch, '%') "
           + "ORDER BY "
           + "CASE WHEN :sort = 'idx' THEN LPAD(CAST(board.idx AS STRING), 10, '0') "
           + "     WHEN :sort = 'created_at' THEN board.createdAt "
           + "     WHEN :sort = 'view_count' THEN LPAD(CAST(board.viewCount AS STRING), 10, '0') "
           + "     WHEN :sort = 'recommend_count' THEN LPAD(CAST(board.recommendCount AS STRING), 10, '0') "
           + "END") // 내림차순 정렬을 선택하는 변수를 추가하여 처리
    List<BoardEntity> findAllSearchedAndOrderBySort(
            @Param("nameSearch") String nameSearch,
            @Param("contentSearch") String contentSearch,
            @Param("userSearch") String userSearch,
            @Param("sort") String sort
    );

    @Modifying
    @Query("UPDATE BoardEntity b SET b.recommendCount = b.recommendCount + 1 WHERE b.idx = :boardIdx")
    void incrementRecommendCount(@Param("boardIdx") Long boardIdx);

    List<BoardEntity> findAllByUserEntity_Idx(Long userIdx);
}
