package com.zip.community.platform.adapter.out.jpa.board;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.board.BoardReaction;
import com.zip.community.platform.domain.board.UserReaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardReactionJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    private Long memberId;


    private Long boardId;

    @Enumerated(EnumType.STRING)
    private UserReaction reactionType;

    // from
    public static BoardReactionJpaEntity from(BoardReaction boardReaction) {
        return BoardReactionJpaEntity.builder()
                .id(boardReaction.getId())
                .memberId(boardReaction.getMemberId())
                .boardId(boardReaction.getBoardId())
                .reactionType(boardReaction.getReactionType())
                .build();
    }

    // toDomain
    public BoardReaction toDomain() {
        return BoardReaction.builder()
                .id(this.id)
                .boardId(this.boardId)
                .memberId(this.memberId)
                .reactionType(this.reactionType)
                .createdAt(this.getCreated())
                .updatedAt(this.getUpdated())
                .build();
    }


}
