package com.zip.community.platform.adapter.out.redis.board.temp;

import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.board.BoardStatistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RedisHash("tempBoard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TempBoardRedisHash{

    /*
        BoardRedisHash와 중복이기에
        요구사항에 맞춰서 수정할 지 말지 정해야 할듯.
     */

    @Id
    private Long id;

    @Indexed
    private Long memberId;

    private Long categoryId;

    private TempBoardSnippetRedisHash boardSnippet;

    private Long created; // 생성일 자동 설정

    private Long updated; // 수정일 자동 설정

    // from 생성자
    public static TempBoardRedisHash from(Board board) {

        // 시간을 Long타입으로 변경하여 저장
        // 임시저장이기에 Now로 저장

        return TempBoardRedisHash.builder()
                .id(board.getId())
                .memberId(board.getMemberId())
                .categoryId(board.getCategoryId())
                .boardSnippet(TempBoardSnippetRedisHash.from(board.getSnippet()))
                .created(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .updated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .build();
    }

    // toDomain
    public Board toDomain() {
        // Long타입을 LocalDateTime으로 변경

        return Board.builder()
                .id(id)
                .memberId(memberId)
                .categoryId(categoryId)
                .snippet(boardSnippet.toDomain())
                .statistics(BoardStatistics.of())
                .createdAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(getCreated()), ZoneId.systemDefault()))
                .updatedAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(getUpdated()), ZoneId.systemDefault()))
                .build();
    }

    // 수정 값 갱신
    public void updateTimestamp() {
        this.updated = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();  // updated 값 갱신
    }
}
