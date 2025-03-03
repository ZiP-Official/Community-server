package com.zip.community.platform.adapter.out.jpa.review;

import com.zip.community.platform.domain.review.ReviewStatistics;
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
public class ReviewStatisticsEntity {

    private long viewCount;
    private long likeCount;

    // from
    public static ReviewStatisticsEntity from(ReviewStatistics statistics) {
        return ReviewStatisticsEntity.builder()
                .viewCount(statistics.getViewCount())
                .likeCount(statistics.getLikeCount())
                .build();
    }

    // toDomain
    public ReviewStatistics toDomain() {
        return ReviewStatistics.builder()
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .build();
    }
}
