package com.zip.community.platform.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
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

    public void addViewCount() {
        this.viewCount++;
    }

    public void removeViewCount() {
        this.viewCount--;
    }

    public void addCommentCount() {
        this.commentCount++;
    }

    public void removeCommentCount() {
        this.commentCount--;
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void removeLikeCount() {
        this.likeCount--;
    }
}
