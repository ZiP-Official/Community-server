package com.zip.community.platform.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentStatistics {

    private int likeCount;
    private int dislikeCount;

    // 생성자
    public static CommentStatistics of() {
        return CommentStatistics.builder()
                .likeCount(0)
                .dislikeCount(0)
                .build();
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void addDislikeCount() {
        this.dislikeCount++;
    }

}
