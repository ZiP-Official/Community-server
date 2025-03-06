package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.BoardReaction;

public interface SaveBoardReactionPort {

    // 좋아요를 저장한다
    void saveLikeBoardReaction(Long boardId, Long userId);

    // 싫어요를 저장한다.
    void saveDisLikeBoardReaction(Long boardId, Long userId);

    // 싱크를 맞춘다
    void synchronizeBoardReaction(BoardReaction boardReaction);

}
