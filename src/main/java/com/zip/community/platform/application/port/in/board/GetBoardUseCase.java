package com.zip.community.platform.application.port.in.board;

import com.zip.community.platform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBoardUseCase {

    // 게시물 상세 조회
    Board getBoardById(Long boardId);

    // 최신 게시물 목록 조회
    Page<Board> getBoards(Pageable pageable);

    // 인기 게시물 목록 조회
    Page<Board> getBoardsView(Pageable pageable);


    // 특정 카테고리에 있는 게시글 조회하기 (댓글은 숫자)
    Page<Board> getBoardsByCategoryId(Long categoryId, Pageable pageable);


}
