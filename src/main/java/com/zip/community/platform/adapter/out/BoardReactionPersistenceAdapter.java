package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.board.BoardReactionJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.repository.BoardReactionJpaRepository;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.domain.board.BoardReaction;
import com.zip.community.platform.domain.board.UserReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardReactionPersistenceAdapter implements LoadBoardReactionPort, SaveBoardReactionPort, RemoveBoardReactionPort {

    private final BoardReactionJpaRepository repository;

    @Override
    public BoardReaction saveBoardReaction(BoardReaction boardReaction) {
        var result = BoardReactionJpaEntity.from(boardReaction);

        return repository.save(result).toDomain();
    }

    @Override
    public Optional<BoardReaction> loadBoardReaction(Long boardId, Long memberId) {

        return repository.findByBoardIdAndMemberId(boardId, memberId)
                .map(BoardReactionJpaEntity::toDomain);
    }

    @Override
    public Optional<BoardReaction> loadBoardReactionByType(Long boardId, Long memberId, UserReaction reactionType) {

        return repository.findByBoardIdAndMemberIdAndReactionType(boardId, memberId, reactionType)
                .map(BoardReactionJpaEntity::toDomain);
    }

    @Override
    public void removeBoardReaction(BoardReaction boardReaction) {

        repository.deleteById(boardReaction.getId());
    }
}
