package com.zip.community.platform.application.port.out.board;


public interface RemoveBoardReactionPort {

    // 좋아요 삭제
    void removeBoardLikeReaction(Long boardId, Long userId);

    // 싫어요 삭제
    void removeBoardDisLikeReaction(Long boardId, Long userId);

    /// 게시글 삭제와 연동되는 부분
    // 글의 삭제와 함께 모두 삭제되는 기능
    void removeAllByBoardId(Long boardId);

    void removeEntity(Long boardId);

    void removeCache(Long boardId);



}
