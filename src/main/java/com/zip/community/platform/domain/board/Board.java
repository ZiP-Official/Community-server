package com.zip.community.platform.domain.board;


import com.zip.community.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseDomain {

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
}
