package com.zip.community.platform.adapter.out.jpa.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, String> {

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = :boardId AND c.parentId IS NULL")
    Page<CommentJpaEntity> findRootCommentsByBoardId(@Param("boardId") Long boardId,  Pageable pageable);

}
