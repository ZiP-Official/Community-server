package com.zip.community.platform.application.port.out.comment;


import com.zip.community.platform.domain.comment.Comment;
import com.zip.community.platform.domain.comment.CommentReaction;

import java.util.List;

public interface SaveCommentReactionPort {

    // 좋아요를 저장한다
    void saveLikeCommentReaction(String commentId, Long userId);

    // 싫어요를 저장한다.
    void saveDisLikeCommentReaction(String commentId, Long userId);

    // 인기 댓글로 저장하기
    void savePinnedComment(List<Comment> comments);

    // 싱크를 맞춘다
    void synchronizeCommentReaction(CommentReaction commentReaction);

}
