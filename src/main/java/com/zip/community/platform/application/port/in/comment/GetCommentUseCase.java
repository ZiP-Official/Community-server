package com.zip.community.platform.application.port.in.comment;

import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetCommentUseCase {

    // 게시별 댓글 조회
    Page<Comment> getByBoardId(Long boardId, Pageable pageable);

}
