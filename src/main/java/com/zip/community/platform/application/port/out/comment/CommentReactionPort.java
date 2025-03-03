package com.zip.community.platform.application.port.out.comment;


import com.zip.community.platform.domain.board.UserReaction;
import com.zip.community.platform.domain.comment.CommentReaction;

import java.util.Optional;

public interface CommentReactionPort {

    // 저장
    CommentReaction saveBoardReaction(CommentReaction reaction);

    // 불러오기
    Optional<CommentReaction> loadBoardReaction(Long commentId, Long memberId);

    Optional<CommentReaction> loadBoardReactionByType(Long commentId, Long memberId, UserReaction reactionType);


    // 삭제
    void removeBoardReaction(CommentReaction reaction);


}
