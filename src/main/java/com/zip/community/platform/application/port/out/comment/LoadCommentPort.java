package com.zip.community.platform.application.port.out.comment;

import com.zip.community.platform.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.Optional;

public interface LoadCommentPort {

    // 존재여부 파악하기
    boolean getCheckedExistComment(String id);

    // 댓글 하나 가져오기
    Optional<Comment> loadCommentById(String id);

    // 게시글에 맞는 댓글 가져오기
    Page<Comment> loadCommentsByBoardId(Long boardId, Pageable pageable);

    /// 대댓글 가져오기
    List<Comment> loadCommentsByCommentId(String parentId);

}
