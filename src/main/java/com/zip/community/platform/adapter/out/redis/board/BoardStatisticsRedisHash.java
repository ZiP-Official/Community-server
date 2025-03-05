package com.zip.community.platform.adapter.out.redis.board;

import com.zip.community.platform.domain.board.BoardStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("boardStatistic")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardStatisticsRedisHash {

    private long viewCount;
    private long commentCount;
    private long likeCount;

    // from
    public static BoardStatisticsRedisHash from(BoardStatistics boardStatistics) {
        return new BoardStatisticsRedisHash
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

}
