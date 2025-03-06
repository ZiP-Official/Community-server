package com.zip.community.platform.application.port.out.board;

public interface LoadBoardReactionPort {

    // 특정 글에 리액션을 남긴게 있는지 확인
    boolean checkBoardReaction(Long boardId, Long memberId);

    // 특정 글에 싫어요 리액션을 남긴게 있는지 확인
    boolean checkBoardLikeReaction(Long boardId, Long memberId);

    // 특정 글에 싫어요 리액션을 남긴게 있는지 확인
    boolean checkBoardDisLikeReaction(Long boardId, Long memberId);

    // 좋아요 개수 가져오기
    long loadBoardLikeCount(Long boardId);

    // 싫어요 개수 가져오기
    long loadBoardDisLikeCount(Long boardId);

    // 좋아요를 누른 사람 목록 가져오기
}
