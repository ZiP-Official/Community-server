package com.zip.community.platform.adapter.in.web.dto.response.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zip.community.platform.domain.estate.Estate;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // NULL 값인 필드는 JSON 응답에서
public class EstateListResponse {

    // 아파트 기본 정보
    private String complexCode;  // 아파트 단지 코드
    private String complexName;  // 아파트 단지명
    private String address;      // 주소


    public static EstateListResponse from(Estate estate) {
        return EstateListResponse.builder()
                .complexCode(estate.getKaptCode())
                .complexName(estate.getKaptName())
                .address(estate.getKaptAddr())
                .build();
    }

    public static EstateListResponse from(Estate estate, int count) {
        return EstateListResponse.builder()
                .complexCode(estate.getKaptCode())
                .complexName(estate.getKaptName())
                .address(estate.getKaptAddr())
                .build();
    }

    public static List<EstateListResponse> from(List<Estate> estates) {
        return estates.stream().map(EstateListResponse::from).toList();
    }



}
