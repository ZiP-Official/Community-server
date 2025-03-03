package com.zip.community.platform.adapter.out.jpa.review;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.review.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private Long memberId;

    @Column(name = "apt_id")
    private String aptId;

    @Embedded
    private ReviewSnippetEntity snippet;

    @Embedded
    private ReviewStatisticsEntity statistics;

    // from
    public static ReviewJpaEntity from(Review review) {
        return ReviewJpaEntity.builder()
                .memberId(review.getMemberId())
                .aptId(review.getAptId()) // MongoDB의 AptDocument ID만 저장
                .snippet(ReviewSnippetEntity.from(review.getSnippet()))
                .statistics(ReviewStatisticsEntity.from(review.getStatistics()))
                .build();
    }

    // toDomain -
    public Review toDomain() {
        return Review.builder()
                .id(this.getId())
                .memberId(this.getMemberId())
                .aptId(this.getAptId())
                .snippet(this.getSnippet().toDomain())
                .statistics(this.getStatistics().toDomain())
                .build();
    }
}
