package com.zip.community.platform.application.port.out.comment;

public interface LoadCommentReactionPort {

    // 특정 댓글에 리액션을 남긴게 있는지 확인
    boolean checkCommentReaction(String commentId, Long memberId);

    // 특정 댓글에 싫어요 리액션을 남긴게 있는지 확인
    boolean checkCommentLikeReaction(String commentId, Long memberId);

    // 특정 댓글에 싫어요 리액션을 남긴게 있는지 확인
    boolean checkCommentDisLikeReaction(String commentId, Long memberId);

    // 좋아요 개수 가져오기
    Long loadCommentLikeCount(String commentId);

    // 싫어요 개수 가져오기
    Long loadCommentDisLikeCount(String commentId);

}
