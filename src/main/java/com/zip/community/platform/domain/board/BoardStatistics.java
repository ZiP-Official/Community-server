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

    public void changeViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public void addCommentCount() {
        this.commentCount++;
    }

    public void removeCommentCount() {
        this.commentCount--;
    }

    public void changeLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void bindStatistics(long viewCount, long commentCount, long likeCount) {
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }

    public void bindViewAndLikeStatistics(long viewCount,long likeCount, long diskLikeCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount - diskLikeCount;
    }

}
