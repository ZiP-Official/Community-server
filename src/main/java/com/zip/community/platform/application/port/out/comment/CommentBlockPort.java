package com.zip.community.platform.application.port.out.comment;

import java.util.Set;

public interface CommentBlockPort {

    // 차단
    void saveUserCommentBlock(Long userId, Long commentId);

    // 차단 목록
    Set<String> getUserCommentBlocks(Long userId);
}
