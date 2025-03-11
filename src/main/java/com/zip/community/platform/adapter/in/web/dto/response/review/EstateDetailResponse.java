package com.zip.community.platform.adapter.in.web.dto.response.review;

import com.zip.community.platform.domain.estate.Estate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstateDetailResponse {

    // 아파트 기본 정보
    private String complexCode;  // 아파트 단지 코드
    private String complexName;  // 아파트 단지명
    private String address;      // 주소



    public static EstateDetailResponse from(Estate estate) {
        return EstateDetailResponse.builder()
                .complexCode(estate.getKaptCode())
                .complexName(estate.getKaptName())
                .address(estate.getKaptAddr())
                .build();
    }
}
