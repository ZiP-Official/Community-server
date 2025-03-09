package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.domain.comment.CommentStatistics;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentStatisticsJpaEntity {

    private int likeCount;
    private int dislikeCount;

    // from
    public static CommentStatisticsJpaEntity from(CommentStatistics statistics) {

        return CommentStatisticsJpaEntity.builder()
                .likeCount(statistics.getLikeCount())
                .dislikeCount(statistics.getDislikeCount())
                .build();
    }

    // toDomain
    public CommentStatistics toDomain(){

        return CommentStatistics.builder()
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .build();
    }
}
