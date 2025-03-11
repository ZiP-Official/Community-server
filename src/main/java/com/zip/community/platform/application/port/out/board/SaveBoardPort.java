package com.zip.community.platform.application.port.out.board;

import com.zip.community.platform.domain.board.Board;

public interface SaveBoardPort {

    // 작성글 저장하는 기능
    Board saveBoard(Board board);

    // 인기게시글 목록에 등록하기
    void saveBoardFavorite(Long boardId);

    // 캐시에 조회수를 증가시키는 로직
    void incrementViewCount(Long boardId);

    // DB와 캐시 사이에 조회수를 매핑시키는 로직
    void syncViewCount(Long boardId);


}
