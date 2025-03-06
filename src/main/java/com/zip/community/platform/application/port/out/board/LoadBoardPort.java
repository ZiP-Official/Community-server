package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoadBoardPort {

    // 게시물 상세 조회
    Optional<Board> loadBoardById(Long boardId);

    // 조회수 조회
    Long loadViewCount(Long boardId);

    // 최신 게시물 목록 조회
    Page<Board> loadBoards(Pageable pageable);

    // 조회수 높은 게시물 목록 조회
    Page<Board> loadBoardsView(Pageable pageable);

    // 좋아요 많은 게시물 목록 조회
    Page<Board> loadBoardsLike(Pageable pageable);

    // 화제 게시물 목록 조회 (조회수 + 댓글 + 반응의 종합적인 계산)
    Page<Board> loadBoardsFavorite(Pageable pageable);

    // 카테고리 바탕의 조회
    Page<Board> loadBoardsByCategoryId(Long categoryId, Pageable pageable);

    // 카테고리 내, 조회수 높은 게시물 목록
    Page<Board> loadBoardsByCategoryIdView(Long categoryId, Pageable pageable);

    // 카테고리 내, 좋아요 많은 게시물 목록 조회
    Page<Board> loadBoardsByCategoryIdLike(Long categoryId, Pageable pageable);

    // 카테고리 내, 화제 게시물 목록 조회
    Page<Board> loadBoardsByCategoryIdFavorite(Long categoryId, Pageable pageable);

}
