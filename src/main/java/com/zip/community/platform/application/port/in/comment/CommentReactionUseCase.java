package com.zip.community.platform.application.port.in.comment;


import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;
import com.zip.community.platform.application.port.in.board.response.ReactionStatus;

public interface CommentReactionUseCase {

    // 좋아요 누르기
    ReactionStatus addLikeReaction(CommentReactionRequest request);

    // 싫어요 누르기
    ReactionStatus addDisLikeReaction(CommentReactionRequest request);



}
