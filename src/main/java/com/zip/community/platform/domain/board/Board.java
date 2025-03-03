package com.zip.community.platform.domain.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private Long id;
    private Long memberId;
    private Long categoryId;

    private BoardSnippet snippet;
    private BoardStatistics statistics;

    // 생성자
    public static Board of(Long memberId, Long categoryId, BoardSnippet snippet, BoardStatistics statistics) {
        return Board.builder()
                .memberId(memberId)
                .snippet(snippet)
                .statistics(statistics)
                .categoryId(categoryId)
                .build();

    }

    // 양방향
    public void addComment() {
        this.getStatistics().addCommentCount();
    }

    public void addLikeReaction() {
        this.getStatistics().addLikeCount();
    }


    public void removeLikeReaction() {
        this.getStatistics().removeLikeCount();
    }
}
