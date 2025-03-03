package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.domain.board.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionJpaRepository extends JpaRepository<CommentReactionJpaEntity, Long> {

    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberId(Long commentId, Long memberId);
    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberIdAndReactionType(Long commentId, Long memberId, UserReaction reactionType);
}
