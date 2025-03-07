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
    public static CommentReaction of(String commentId, Long memberId, UserReaction reactionType) {
        return CommentReaction.builder()
                .commentId(commentId)
                .memberId(memberId)
                .reactionType(reactionType)
                .build();
    }

    // 좋아요 형태에 따라서, statics 값 변화
    private static void checkUserReaction(Comment comment, UserReaction reactionType) {
        if(reactionType == UserReaction.LIKE) {
            comment.getStatistics().addLikeCount();
        } else {
            comment.getStatistics().addDislikeCount();
        }
    }
}
