package com.zip.community.platform.application.port.out.comment;

import com.zip.community.platform.domain.comment.Comment;

public interface SaveCommentPort {

    Comment saveComment(Comment comment);

}
