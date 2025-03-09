package com.zip.community.platform.adapter.out.jpa.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, String> {

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = :boardId AND c.parentId IS NULL")
    Page<CommentJpaEntity> findRootCommentsByBoardId(@Param("boardId") Long boardId,  Pageable pageable);

    /// 부모 아이디를 통해서 자식 가져오기
    List<CommentJpaEntity> findCommentByParentId(String parentId);

    /// 게시글 아이디를 통해서 모든 댓글 가져오기
    List<CommentJpaEntity> findCommentByBoardId(Long boardId);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.id IN :ids")
    List<CommentJpaEntity> findAllByIds(List<String> ids);

}
