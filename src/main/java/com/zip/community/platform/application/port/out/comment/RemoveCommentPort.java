package com.zip.community.platform.application.port.out.comment;

public interface RemoveCommentPort {

    // 댓글 지우기
    void removeComment(String id);

    /// 게시글 삭제와 연동되는 부분
    void removeCommentByBoardId(Long boardId);
}
