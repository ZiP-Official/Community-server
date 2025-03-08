package com.zip.community.platform.domain.board;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BoardReaction extends BaseDomain {

    private Long id;
    private Long memberId;
    private Long boardId;
    private UserReaction reactionType;

    // 생성자
    public static BoardReaction of(Long boardId, Long member, UserReaction reactionType) {
        return BoardReaction.builder()
                .boardId(boardId)
                .memberId(member)
                .reactionType(reactionType)
                .build();
    }
}
