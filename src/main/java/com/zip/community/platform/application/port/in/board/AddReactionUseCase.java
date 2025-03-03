package com.zip.community.platform.application.port.in.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.domain.board.BoardReaction;

public interface AddReactionUseCase {

    // 감정 표현 누르기
    BoardReaction addReaction(BoardReactionRequest request);

}
