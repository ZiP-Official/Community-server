package com.zip.community.platform.application.port.out.comment;

public interface RemoveCommentPort {

    // 댓글 지우기
    void removeComment(String id);

    /// 게시글 삭제와 연동되는 부분
    // 글의 삭제와 함께 모두 삭제되는 기능
    void removeAllByBoardId(Long boardId);

    // 영속성 삭제
    void removeEntity(Long boardId);

    // 캐시만 삭제
    void removeCache(Long boardId);


}
