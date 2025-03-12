package com.zip.community.platform.application.port.out.comment;


public interface RemoveCommentReactionPort {

    // 좋아요 삭제
    void removeCommentLikeReaction(String commentId, Long userId);

    // 싫어요 삭제
    void removeCommentDisLikeReaction(String commentId, Long userId);


    /// 게시글에서 사용할 내용
    // 글의 삭제와 함께 모두 삭제되는 기능
    void removeAllByBoardId(Long boardId);

    // 영속성 삭제
    void removeEntity(Long boardId);

    // 캐시만 삭제
    void removeCache(Long boardId);

}
