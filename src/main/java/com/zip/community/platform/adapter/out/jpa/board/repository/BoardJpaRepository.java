package com.zip.community.platform.adapter.out.jpa.board.repository;

import com.zip.community.platform.adapter.out.jpa.board.BoardJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {

    // 카테고리 아이디를 바탕으로 게시글 조회
    @Query("""
    SELECT b
    FROM BoardJpaEntity b
    WHERE b.categoryId = :categoryId and b.deleted = false
""")
    Page<BoardJpaEntity> findBoardJpaEntitiesByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT b FROM BoardJpaEntity b WHERE b.categoryId IN :categories AND b.deleted=false ")
    Page<BoardJpaEntity> findBoardByCategories(@Param("categories") List<Long> categories, Pageable pageable);


    // 최신 게시글 조회
    @Query("select b from BoardJpaEntity b WHERE b.deleted=false ORDER BY b.created DESC ")
    Page<BoardJpaEntity> findBoardsByRecent(Pageable pageable);

    // 존재여부
    @Query("SELECT COUNT(b) > 0 FROM BoardJpaEntity b WHERE b.id = :id AND b.deleted = false")
    boolean existsById(@Param("id") Long id);



    @Query("select b.memberId from BoardJpaEntity b where b.id = :boardId AND b.deleted=false ")
    Optional<Long> getMemberIdByBoardId(Long boardId);



}
