package com.zip.community.platform.adapter.out.redis.board;

import com.zip.community.platform.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BoardRedisHash {

    @Id
    private Long id;

    @Indexed
    private Long memberId;

    private Long categoryId;

    private BoardSnippetRedisHash boardSnippet;

    private BoardStatisticsRedisHash boardStatistics;

    // from 생성자
    public static BoardRedisHash from(Board board) {

        return BoardRedisHash.builder()
                .id(board.getId())
                .memberId(board.getMemberId())
                .categoryId(board.getCategoryId())
                .boardSnippet(BoardSnippetRedisHash.from(board.getSnippet()))
                .boardStatistics(BoardStatisticsRedisHash.from(board.getStatistics()))
                .build();
    }

    // toDomain
    public Board toDomain() {
        return Board.builder()
                .id(id)
                .memberId(memberId)
                .categoryId(categoryId)
                .snippet(boardSnippet.toDomain())
                .statistics(boardStatistics.toDomain())
                .build();
    }
}
