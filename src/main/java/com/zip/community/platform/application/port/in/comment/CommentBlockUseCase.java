package com.zip.community.platform.application.port.in.comment;

public interface CommentBlockUseCase {

    // 댓글 차단하기
    void blockComment(Long memberId, Long commentId);
}
