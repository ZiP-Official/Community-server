package com.zip.community.platform.adapter.out.jpa.board.repository;

import com.zip.community.platform.adapter.out.jpa.board.BoardReactionJpaEntity;
import com.zip.community.platform.domain.board.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardReactionJpaRepository extends JpaRepository<BoardReactionJpaEntity, Long> {

    Optional<BoardReactionJpaEntity> findByBoardIdAndMemberId(Long boardId, Long memberId);

    Optional<BoardReactionJpaEntity> findByBoardIdAndMemberIdAndReactionType(Long boardId, Long memberId, UserReaction reactionType);

    @Query("select case when count(b) > 0 then true else false end from BoardReactionJpaEntity b where b.boardId = :boardId and b.memberId = :memberId and b.reactionType = :reactionType")
    boolean existsByBoardIdAndMemberIdAndReactionType(@Param("boardId") Long boardId, @Param("memberId") Long memberId, @Param("reactionType") UserReaction reactionType);

    @Query("select count(b) from BoardReactionJpaEntity b where b.boardId = :boardId and b.reactionType = :reactionType")
    long countByBoardIdAndReactionType(@Param("boardId") Long boardId, @Param("reactionType") UserReaction reactionType);

}
