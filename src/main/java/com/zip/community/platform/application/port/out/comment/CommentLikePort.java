package com.zip.community.platform.application.port.out.comment;

public interface CommentLikePort {

    // 좋아요 수 가져오기
    Long getCommentLikeCount(Long commentId);
}
