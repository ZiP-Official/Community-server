package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.domain.board.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionJpaRepository extends JpaRepository<CommentReactionJpaEntity, String> {

    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberId(String commentId, Long memberId);
    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberIdAndReactionType(String commentId, Long memberId, UserReaction reactionType);
}
