package com.zip.community.platform.adapter.in.web.dto.response;

import com.zip.community.platform.domain.board.UserReaction;
import com.zip.community.platform.domain.comment.CommentReaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentReactionResponse {

    private Long author;

    private String commentId;

    @Enumerated(EnumType.STRING)
    private UserReaction reactionType;

    // 생성자
    public static CommentReactionResponse from(CommentReaction commentReaction) {
        return CommentReactionResponse.builder()
                .author(commentReaction.getMemberId())
                .commentId(commentReaction.getCommentId())
                .reactionType(commentReaction.getReactionType())
                .build();
    }

}
