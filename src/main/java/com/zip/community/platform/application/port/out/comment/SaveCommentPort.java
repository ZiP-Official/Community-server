package com.zip.community.platform.application.port.out.comment;

import com.zip.community.platform.domain.comment.Comment;

public interface SaveCommentPort {

    Comment saveComment(Comment comment);

    // 캐시에 조회수를 증가시키는 로직
    void incrementCommentCount(Long boardId);

    /// 데이터 일관성 유지를 위한
    void syncBoardReaction(Long boardId);


}
