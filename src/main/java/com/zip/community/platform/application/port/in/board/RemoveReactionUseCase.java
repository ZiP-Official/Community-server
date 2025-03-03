package com.zip.community.platform.application.port.in.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;

public interface RemoveReactionUseCase {

    // 감정 표현 지우기
    void removeReaction(BoardReactionRequest request);


}
