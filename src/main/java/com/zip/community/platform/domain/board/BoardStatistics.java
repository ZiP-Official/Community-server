package com.zip.community.platform.domain.board;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardStatistics {
    private long viewCount;
    private long commentCount;
    private long likeCount;

    // 생성자
    public static BoardStatistics of() {
        return BoardStatistics.builder()
            .likeCount(0)
            .viewCount(0)
            .commentCount(0)
            .build();
    }

    /// 도메인 상으로 값이 변한것만 보여주기 위해서 사용
    public void bindStatistics(long viewCount, long commentCount, long likeCount, long diskLikeCount) {
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount - diskLikeCount;
    }

}
