package com.zip.community.platform.adapter.out.jpa.board.repository;

import com.zip.community.platform.adapter.out.jpa.board.BoardJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {

    @Query("""
    SELECT b
    FROM BoardJpaEntity b
    WHERE b.categoryId = :categoryId
""")
    Page<BoardJpaEntity> findBoardJpaEntitiesByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);



}
