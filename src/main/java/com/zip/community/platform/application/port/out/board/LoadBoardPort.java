package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface LoadBoardPort {

    // 존재하는지 확인
    boolean existBoard(Long boardId);

    // 게시물 상세 조회
    Optional<Board> loadBoardById(Long boardId);

    // 조회수 조회
    Long loadViewCount(Long boardId);

    // 전체 내, 최신 게시물 목록 조회
    Page<Board> loadBoards(Pageable pageable);

    // 전체 내, 인기 게시물 목록 조회 (조회수 + 댓글 + 반응의 종합적인 계산)
    Page<Board> loadBoardsFavorite(Pageable pageable);

    // 카테고리 바탕의 조회 , 하위 카테고리 게시글까지 나와야 한다.
    Page<Board> loadBoardsByCategories(List<Long> categories, Pageable pageable);

    // 카테고리 내, 인기 게시물 목록 조회
    Page<Board> loadBoardsByCategoryIdFavorite(Long categoryId, Pageable pageable);

    // 인기 게시물 목록 중 하나인지 조회
    boolean checkBoardFavorite(Long boardId);

    // 글 작성자 판단 위한 작성자 id 가져오기
    Optional<Long> loadWriterIdByBoardId(Long boardId);

    /// 제작은 해두었지만, 사용하지 않는 기능

    // 최하위 카테고리 바탕의 조회
    Page<Board> loadBoardsByCategoryId(Long categoryId, Pageable pageable);
}
