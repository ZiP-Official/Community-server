package com.zip.community.platform.application.port.in.board;

import com.zip.community.platform.adapter.in.web.dto.request.board.TempBoardRequest;
import com.zip.community.platform.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface TempBoardUseCase {

    /*
        게시글 임시저장을 위한 유즈케이스 입니다.
        게시글 임시생성과,
        유저 아이디별 생성한 게시글으르 Cache에서 가져오는 기능을 수행합니다.
     */

    void createTempBoard(TempBoardRequest request);

    List<Board> getTempBoards(Long userId);

    Optional<Board> getTempBoard(Long boardId, int index);

    void deleteTempBoard(Long boardId, int index);

}
