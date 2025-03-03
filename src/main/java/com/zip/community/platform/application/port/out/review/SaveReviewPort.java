package com.zip.community.platform.application.port.out.review;


import com.zip.community.platform.domain.review.Review;

public interface SaveReviewPort {

    Review saveReview(Review review);

}
