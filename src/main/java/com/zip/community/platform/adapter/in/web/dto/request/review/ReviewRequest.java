package com.zip.community.platform.adapter.in.web.dto.request.review;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ReviewRequest {

    // 멤버 관리
    private Long memberId;

    // 원하는 아파트 관리
    private String aptId;

    // 리뷰 내용
    private String title;
    private String content;

    // 리뷰 평점
    private int transport;
    private int environment;
    private int apartmentManagement;
    private int livingEnvironment;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

}
