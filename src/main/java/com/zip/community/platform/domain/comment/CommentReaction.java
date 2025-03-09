package com.zip.community.platform.domain.comment;

import com.zip.community.platform.domain.board.UserReaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentReaction {

    private String id;
    private Long memberId;
    private String commentId;
    private UserReaction reactionType;

    // 생성자
    public static CommentReaction of(String id, String commentId, Long memberId, UserReaction reactionType) {

        return CommentReaction.builder()
                .id(id)
                .commentId(commentId)
                .memberId(memberId)
                .reactionType(reactionType)
                .build();
    }
}

