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

    // 인기 게시물 목록 조회
    Page<Board> loadBoardsFavorite(Pageable pageable);

    // 최신 게시물 목록 조회
    Page<Board> loadBoards(Pageable pageable);


    Page<Board> loadByCategoryId(Long categoryId, Pageable pageable);
}
