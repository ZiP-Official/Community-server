package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.domain.board.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentReactionJpaRepository extends JpaRepository<CommentReactionJpaEntity, String> {

    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberId(String commentId, Long memberId);
    Optional<CommentReactionJpaEntity> findByCommentIdAndMemberIdAndReactionType(String commentId, Long memberId, UserReaction reactionType);

    @Query("select count(c) from CommentReactionJpaEntity c where c.commentId = :commentId and c.reactionType = :reactionType")
    long countByCommentIdAndReactionType(@Param("commentId") String commentId, @Param("reactionType") UserReaction reactionType);

    @Query("select case when count(c) > 0 then true else false end from CommentReactionJpaEntity c where c.commentId = :commentId and c.memberId = :memberId and c.reactionType = :reactionType")
    boolean existsByCommentIdAndMemberIdAndReactionType(@Param("commentId") String commentId, Long memberId, UserReaction reactionType);
}
