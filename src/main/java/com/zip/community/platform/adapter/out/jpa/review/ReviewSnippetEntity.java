package com.zip.community.platform.adapter.out.jpa.review;

import com.zip.community.platform.domain.review.ReviewSnippet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewSnippetEntity {

    private String title;
    private String content;

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

    // from
    public static ReviewSnippetEntity from(ReviewSnippet snippet) {
        return ReviewSnippetEntity.builder()
                .transport(snippet.getTransport())
                .environment(snippet.getEnvironment())
                .apartmentManagement(snippet.getApartmentManagement())
                .livingEnvironment(snippet.getLivingEnvironment())
                .overall(snippet.getOverall())
                .build();
    }

    // toDomain
    public ReviewSnippet toDomain() {
        return ReviewSnippet.builder()
                .transport(this.getTransport())
                .environment(this.getEnvironment())
                .apartmentManagement(this.getApartmentManagement())
                .livingEnvironment(this.getLivingEnvironment())
                .overall(this.getOverall())
                .build();

    }

}
