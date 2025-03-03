package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.domain.board.UserReaction;
import com.zip.community.platform.domain.comment.CommentReaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentReactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    private Long memberId;

    private Long commentId;

    @Enumerated(EnumType.STRING)
    private UserReaction reactionType;

    // from
    public static CommentReactionJpaEntity from(CommentReaction commentReaction) {
        return CommentReactionJpaEntity.builder()
                .memberId(commentReaction.getMemberId())
                .commentId(commentReaction.getCommentId())
                .reactionType(commentReaction.getReactionType())
                .build();
    }

    // toDomain
    public CommentReaction toDomain() {
        return CommentReaction.builder()
                .id(this.getId())
                .memberId(this.getMemberId())
                .commentId(this.getCommentId())
                .reactionType(this.getReactionType())
                .build();
    }

}
