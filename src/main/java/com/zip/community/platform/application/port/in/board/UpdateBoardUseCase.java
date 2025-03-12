package com.zip.community.platform.application.port.in.board;

import com.zip.community.platform.adapter.in.web.dto.request.board.BoardUpdateRequest;
import com.zip.community.platform.domain.board.Board;

public interface UpdateBoardUseCase {
    /*
        게시글 수정 유즈케이스
     */

    // 유저의 게시글 수정
    Board updateBoard(BoardUpdateRequest request);

    /// 데이터 동기화 업데이트
    void syncData(Long boardId);
}
