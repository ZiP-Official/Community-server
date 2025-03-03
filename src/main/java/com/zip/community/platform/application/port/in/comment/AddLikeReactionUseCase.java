package com.zip.community.platform.application.port.in.comment;


import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;
import com.zip.community.platform.domain.comment.CommentReaction;

public interface AddLikeReactionUseCase {

    // 좋아요 추가하기
    CommentReaction addReaction(CommentReactionRequest request);




}
