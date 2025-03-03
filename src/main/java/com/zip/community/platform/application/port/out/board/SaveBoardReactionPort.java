package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.BoardReaction;

public interface SaveBoardReactionPort {

    BoardReaction saveBoardReaction(BoardReaction boardReaction);

}
