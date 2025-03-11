package com.zip.community.platform.application.port.out.comment;


public interface RemoveCommentReactionPort {

    // 좋아요 삭제
    void removeCommentLikeReaction(String commentId, Long userId);

    // 싫어요 삭제
    void removeCommentDisLikeReaction(String commentId, Long userId);


    /// 게시글에서 사용할 내용
    void removeAllByBoardId(Long boardId);
}
