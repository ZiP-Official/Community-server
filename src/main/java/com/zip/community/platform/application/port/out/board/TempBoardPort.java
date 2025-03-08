package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface TempBoardPort {

    /*
        Cache에서 값을 가져오는 역할을 수행합니다.
     */

    /// Save
    // 게시글 저장하기
    Board saveTempBoard(Board board);


    /// Load
    // 임시 저장 게시글 상세 조회
    Optional<Board> getTempBoard(Long userId,int index);

    // 임시 저장 게시글 목록 조회
    List<Board> getTempBoards(Long userId);

    /// Delete
    void deleteTempBoard(Long userId, int index);

}
