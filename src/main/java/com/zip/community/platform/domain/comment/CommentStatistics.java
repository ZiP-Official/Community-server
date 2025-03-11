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

    private Long likeCount;
    private Long dislikeCount;

    // 생성자
    public static CommentStatistics of() {
        return CommentStatistics.builder()
                .likeCount(0L)
                .dislikeCount(0L)
                .build();
    }

    // 비즈니스 로직
    public void bindReactionCount(Long likeCount, Long dislikeCount) {
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

}
