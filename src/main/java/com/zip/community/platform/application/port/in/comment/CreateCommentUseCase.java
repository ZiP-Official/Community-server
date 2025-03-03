package com.zip.community.platform.application.port.in.comment;


import com.zip.community.platform.adapter.in.web.dto.request.board.CommentRequest;
import com.zip.community.platform.domain.comment.Comment;

public interface CreateCommentUseCase {

    // 댓글 등록
    Comment createComment(CommentRequest commentRequest);

}
