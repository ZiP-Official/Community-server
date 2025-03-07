package com.zip.community.platform.adapter.in.web.dto.request.board;

import com.zip.community.platform.domain.board.UserReaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class CommentReactionRequest {

    private Long id;

    private Long memberId;

    private String commentId;

    @Enumerated(EnumType.STRING)
    private UserReaction reactionType;

}
