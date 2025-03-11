package com.zip.community.platform.adapter.in.web.dto.response.review;

import com.zip.community.platform.domain.review.ReviewSnippet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {

    // 교통 여건 평점
    private int transport;

    // 주변 환경 평점
    private int environment;

    // 단지 관리 평점
    private int apartmentManagement;

    // 거주 환경 평점
    private int livingEnvironment;

    // 전체 평점
    private int overall;

    public static RatingResponse of (ReviewSnippet snippet) {
        return RatingResponse.builder()
                .transport(snippet.getTransport())
                .environment(snippet.getEnvironment())
                .apartmentManagement(snippet.getApartmentManagement())
                .livingEnvironment(snippet.getLivingEnvironment())
                .overall(snippet.getOverall())
                .build();
    }

}
