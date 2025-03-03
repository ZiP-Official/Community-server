package com.zip.community.platform.domain.review;

import com.zip.community.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseDomain {

    private Long id;

    private Long memberId;
    private String aptId;

    private ReviewSnippet snippet;
    private ReviewStatistics statistics;

    // 생성 로직
    public static Review of(Long memberId, String aptId, ReviewSnippet snippet, ReviewStatistics statistics) {

        return Review.builder()
                .memberId(memberId)
                .aptId(aptId)
                .snippet(snippet)
                .statistics(statistics)
                .build();
    }

}
