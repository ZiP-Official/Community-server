package com.zip.community.platform.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewSnippet {

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

    // 생성 로직
    public static ReviewSnippet of(Integer transport, Integer environment, Integer apartmentManagement, Integer livingEnvironment) {
        return ReviewSnippet.builder()
                .transport(Objects.requireNonNullElse(transport, 0))
                .environment(Objects.requireNonNullElse(environment, 0))
                .apartmentManagement(Objects.requireNonNullElse(apartmentManagement, 0))
                .livingEnvironment(Objects.requireNonNullElse(livingEnvironment, 0))
                .build();
    }


}
