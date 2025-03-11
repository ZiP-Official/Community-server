package com.zip.community.platform.adapter.out.jpa.board;

import com.zip.community.platform.domain.board.BoardStatistics;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardStatisticsJpaEntity {

    private long viewCount;
    private long commentCount;
    private long likeCount;

    // from
    public static BoardStatisticsJpaEntity from(BoardStatistics boardStatistics) {
        return new BoardStatisticsJpaEntity
                (boardStatistics.getViewCount(), boardStatistics.getCommentCount(), boardStatistics.getLikeCount());
    }

    // toDomain
    public BoardStatistics toDomain(){
        return BoardStatistics.builder()
                .viewCount(this.viewCount)
                .commentCount(this.commentCount)
                .likeCount(this.likeCount)
                .build();
    }

    ///  비즈니스 로직
    public void changeViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

}
