package com.zip.community.platform.adapter.in.web.dto.response;

import com.zip.community.platform.domain.review.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ReviewResponse {

    // 아파트 정보
    private String aptId;

    // 리뷰 작성자
    private Long author;

    // 리뷰 내용
    private String title;
    private String content;

    // 리뷰 평점
    private RatingResponse rating;

    private long viewCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<String> uploadFileNames = List.of(); // Immutable 빈 리스트 사용

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .aptId(review.getAptId())
                .author(review.getMemberId())
                .title(review.getSnippet().getTitle())
                .content(review.getSnippet().getContent())
                .rating(RatingResponse.of(review.getSnippet()))
                .viewCount(review.getStatistics().getViewCount())
                .build();
    }

    public static List<ReviewResponse> from(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }
}
