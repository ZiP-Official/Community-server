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
public class ReviewStatistics {

    private long viewCount;
    private long likeCount;

    // 생성 로직
    public static ReviewStatistics of(Integer viewCount, Integer likeCount) {
        return ReviewStatistics.builder()
                .viewCount(Objects.requireNonNullElse(viewCount, 0))
                .likeCount(Objects.requireNonNullElse(likeCount, 0))
                .build();
    }


    // 로직
    public void addCount() {
        this.viewCount++;
    }

}
