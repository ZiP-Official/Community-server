package com.zip.community.platform.application.port.out.board;


public interface RemoveBoardReactionPort {

    // 좋아요 삭제
    void removeBoardLikeReaction(Long boardId, Long userId);

    // 싫어요 삭제
    void removeBoardDisLikeReaction(Long boardId, Long userId);


}
