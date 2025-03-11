package com.zip.community.platform.application.port.out.board;


public interface RemoveBoardReactionPort {

    // 좋아요 삭제
    void removeBoardLikeReaction(Long boardId, Long userId);

    // 싫어요 삭제
    void removeBoardDisLikeReaction(Long boardId, Long userId);


    /// 게시글 삭제와 연동되는 부분
    // 게시글에 해당 하는 내용 다 삭제하기
    void removeAllByBoardId(Long boardId);
}
