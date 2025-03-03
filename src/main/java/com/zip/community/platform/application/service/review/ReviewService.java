package com.zip.community.platform.application.service.review;

import com.zip.community.platform.adapter.in.web.dto.request.review.ReviewRequest;
import com.zip.community.platform.application.port.in.review.CreateReviewUseCase;
import com.zip.community.platform.application.port.in.review.GetReviewUseCase;
import com.zip.community.platform.application.port.in.review.RemoveReviewUseCase;
import com.zip.community.platform.application.port.out.user.LoadMemberPort;
import com.zip.community.platform.application.port.out.review.LoadReviewPort;
import com.zip.community.platform.application.port.out.review.RemoveReviewPort;
import com.zip.community.platform.application.port.out.review.SaveReviewPort;
import com.zip.community.platform.domain.review.ReviewSnippet;
import com.zip.community.platform.domain.review.ReviewStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.zip.community.platform.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ReviewService implements CreateReviewUseCase, GetReviewUseCase, RemoveReviewUseCase {

    private final LoadReviewPort loadReviewPort;
    private final SaveReviewPort saveReviewPort;
    private final RemoveReviewPort removeReviewPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    public Review createReview(ReviewRequest request) {

        // 정보 생성
        ReviewSnippet snippet = ReviewSnippet.of(request.getTransport(), request.getEnvironment(), request.getApartmentManagement(), request.getLivingEnvironment());
        ReviewStatistics reviewStatistics = ReviewStatistics.of(0, 0);

        // 리뷰 생성
        Review result = Review.of(request.getMemberId(), request.getAptId(), snippet, reviewStatistics);

        return saveReviewPort.saveReview(result);
    }

    @Override
    public Optional<Review> getReview(Long id) {
        Review review = loadReviewPort.loadReview(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰가 존재하지 않습니다."));

        // API 호출할 때마다 조회수 증가
        review.getStatistics().addCount();
        saveReviewPort.saveReview(review);

        return loadReviewPort.loadReview(id);
    }

    // 최신순
    @Override
    public Page<Review> getReviews(String aptId, Pageable pageable) {

        return loadReviewPort.getReviews(aptId, pageable);
    }

    // 높은 점수별
    @Override
    public Page<Review> getReviewsByRating(String aptId, Pageable pageable) {

        return loadReviewPort.getReviewsByRating(aptId, pageable);
    }


    // 유저별
    @Override
    public Page<Review> getReviewsByMember(Long memberId, Pageable pageable) {

        return loadReviewPort.getReviewsByMember(memberId, pageable);
    }


    @Override
    public void removeReview(Review review) {

        removeReviewPort.removeReview(review);

    }


}
