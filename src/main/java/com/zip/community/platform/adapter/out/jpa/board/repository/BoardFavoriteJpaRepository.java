package com.zip.community.platform.adapter.out.jpa.board.repository;

import com.zip.community.platform.adapter.out.jpa.board.BoardFavoriteJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.BoardJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardFavoriteJpaRepository extends JpaRepository<BoardFavoriteJpaEntity, Long> {

    boolean existsByBoardId(Long boardId);

    @Query("select b from BoardFavoriteJpaEntity b order by b.created DESC ")
    Page<BoardFavoriteJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);


}
