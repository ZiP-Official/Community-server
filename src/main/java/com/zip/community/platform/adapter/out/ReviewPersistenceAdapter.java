package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.review.ReviewJpaEntity;
import com.zip.community.platform.adapter.out.jpa.review.ReviewJpaRepository;
import com.zip.community.platform.application.port.out.review.LoadReviewPort;
import com.zip.community.platform.application.port.out.review.RemoveReviewPort;
import com.zip.community.platform.application.port.out.review.SaveReviewPort;
import com.zip.community.platform.domain.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements LoadReviewPort, SaveReviewPort, RemoveReviewPort {

    private final ReviewJpaRepository repository;

    @Override
    public Review saveReview(Review review) {
        var entity = ReviewJpaEntity.from(review);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public Optional<Review> loadReview(Long id) {
        return repository.findById(id)
                .map(ReviewJpaEntity::toDomain);
    }

    @Override
    public Page<Review> getReviewsByMember(Long memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable)
                .map(ReviewJpaEntity::toDomain);
    }

    @Override
    public Page<Review> getReviews(String aptId, Pageable pageable) {
        return repository.findByAptId(aptId, pageable)
                .map(ReviewJpaEntity::toDomain);
    }

    @Override
    public Page<Review> getReviewsByRating(String aptId, Pageable pageable) {
        return repository.findByAptIdOrderBySnippetOverallDesc(aptId, pageable)
                .map(ReviewJpaEntity::toDomain);
    }

    @Override
    public void removeReview(Review review) {
        repository.deleteById(review.getId());
    }
}
