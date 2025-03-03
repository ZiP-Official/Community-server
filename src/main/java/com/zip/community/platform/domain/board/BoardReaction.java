package com.zip.community.platform.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardReaction {

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

    // 메소드 따로 빼기
    private static void checkUserReaction(Board board, UserReaction reactionType) {
        if(reactionType == UserReaction.LIKE) {
            board.addLikeReaction();

        } else {
            board.removeLikeReaction();
        }
    }
}
