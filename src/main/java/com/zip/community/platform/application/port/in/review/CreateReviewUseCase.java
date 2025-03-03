package com.zip.community.platform.application.port.in.review;


import com.zip.community.platform.adapter.in.web.dto.request.review.ReviewRequest;
import com.zip.community.platform.domain.review.Review;

public interface CreateReviewUseCase {

    Review createReview(ReviewRequest reviewRequest);


}
