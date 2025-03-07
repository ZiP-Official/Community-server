package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaRepository;
import com.zip.community.platform.application.port.out.comment.CommentReactionPort;
import com.zip.community.platform.domain.board.UserReaction;
import com.zip.community.platform.domain.comment.CommentReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentReactionPersistenceAdapter implements CommentReactionPort {

    private final CommentReactionJpaRepository repository;

    @Override
    public CommentReaction saveBoardReaction(CommentReaction reaction) {
        var commentReaction = CommentReactionJpaEntity.from(reaction);

        return repository.save(commentReaction)
                .toDomain();
    }

    @Override
    public Optional<CommentReaction> loadBoardReaction(String commentId, Long memberId) {

        return repository.findByCommentIdAndMemberId(commentId, memberId)
                .map(CommentReactionJpaEntity::toDomain);
    }

    @Override
    public Optional<CommentReaction> loadBoardReactionByType(String commentId, Long memberId, UserReaction reactionType) {
        return repository.findByCommentIdAndMemberIdAndReactionType(commentId, memberId, reactionType)
                .map(CommentReactionJpaEntity::toDomain);
    }

    @Override
    public void removeBoardReaction(CommentReaction reaction) {

        repository.deleteById(reaction.getId());

    }
}
