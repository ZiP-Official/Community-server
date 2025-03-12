package com.zip.community.platform.application.port.out.board;

public interface RemoveBoardPort {

    // 게시글 삭제 (부가요소들까지 전부 삭제)
    void removeBoard(Long boardId);

    // 엔티티만 삭제
    void removeEntity(Long boardId);

    // 캐시만 삭제
    void removeCache(Long boardId);

}
