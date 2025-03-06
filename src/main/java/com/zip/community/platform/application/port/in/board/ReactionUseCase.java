package com.zip.community.platform.application.port.in.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.in.board.response.ReactionStatus;

public interface ReactionUseCase {

    /*
        좋아요 처리의 요구사항
        - 한 명의 유저가 하나의 게시글에 좋아요를 여러번 누를수 없다.
        - 한 명의 유저가 좋아요를 누른 상태에서, 싫어요를 누르면 자동으로 좋아요가 취소가 된다. (반대의 경우도 동일)
     */

    // 좋아요 누르기
    ReactionStatus addLikeReaction(BoardReactionRequest request);

    // 싫어요 누르기
    ReactionStatus addDisLikeReaction(BoardReactionRequest request);


}
