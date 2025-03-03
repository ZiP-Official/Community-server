package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.BoardReaction;
import com.zip.community.platform.domain.board.UserReaction;

import java.util.Optional;

public interface LoadBoardReactionPort {

    Optional<BoardReaction> loadBoardReaction(Long boardId, Long memberId);
    Optional<BoardReaction> loadBoardReactionByType(Long boardId, Long memberId, UserReaction reactionType);
}
