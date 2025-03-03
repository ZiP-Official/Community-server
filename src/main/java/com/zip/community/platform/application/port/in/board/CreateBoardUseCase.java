package com.zip.community.platform.application.port.in.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.domain.board.Board;

public interface CreateBoardUseCase {

    // 게시물 생성하기 (카테고리 및 사진과 함께)
    Board createBoard(BoardRequest request);



}
