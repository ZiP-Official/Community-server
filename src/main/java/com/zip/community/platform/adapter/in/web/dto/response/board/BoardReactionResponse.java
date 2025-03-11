package com.zip.community.platform.adapter.in.web.dto.response.board;

import com.zip.community.platform.domain.board.BoardReaction;
import com.zip.community.platform.domain.board.UserReaction;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardReactionResponse {

    private Long author;

    private Long boardId;

    @Enumerated(EnumType.STRING)
    private UserReaction reactionType;

    // 생성자
    public static BoardReactionResponse from(BoardReaction boardReaction) {
        return BoardReactionResponse.builder()
                .author(boardReaction.getMemberId())
                .boardId(boardReaction.getBoardId())
                .reactionType(boardReaction.getReactionType())
                .build();
    }

}
