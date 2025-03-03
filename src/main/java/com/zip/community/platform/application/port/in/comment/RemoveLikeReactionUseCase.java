package com.zip.community.platform.application.port.in.comment;

import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;

public interface RemoveLikeReactionUseCase {

    // 좋아요 제거하기
    void removeReaction(CommentReactionRequest request);



}
