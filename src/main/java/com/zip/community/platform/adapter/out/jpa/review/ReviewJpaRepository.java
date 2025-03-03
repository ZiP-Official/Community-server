package com.zip.community.platform.adapter.out.jpa.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {

    // 멤버별 리뷰 조회 (페이징)
    Page<ReviewJpaEntity> findByMemberId(Long memberId, Pageable pageable);

    // 아파트별 리뷰 조회 (페이징)
    Page<ReviewJpaEntity> findByAptId(String aptId, Pageable pageable);

    // 아파트별 리뷰 (전체 평점 높은 순, 페이징)
    Page<ReviewJpaEntity> findByAptIdOrderBySnippetOverallDesc(String aptId, Pageable pageable);
}
