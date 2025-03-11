package com.zip.community.platform.application.port.in.comment;

import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface GetCommentUseCase {

    // 게시별 댓글 조회
    Page<Comment> getByBoardId(Long boardId, Pageable pageable);

    // 게시글별 인기 댓글 조회
    List<Comment> getPinnedComments(Long boardId);

}
