package com.zip.community.platform.application.port.in.board;

import com.zip.community.platform.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBoardInfoUseCase {

    // 특정 카테고리에 있는 게시글 조회하기 (댓글은 숫자)
    Page<Board> getByCategoryId(Long categoryId, Pageable pageable);

    // 게시물 상세 조회
    Board getOneInfo(Long boardId);

}
