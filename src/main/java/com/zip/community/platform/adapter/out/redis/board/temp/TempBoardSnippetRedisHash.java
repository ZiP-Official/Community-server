package com.zip.community.platform.adapter.out.redis.board.temp;

import com.zip.community.platform.domain.board.BoardSnippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("tempBoardSnippet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TempBoardSnippetRedisHash {

    private String title;
    private String content;
    private String thumbnailUrl;

    // from
    public static TempBoardSnippetRedisHash from(BoardSnippet boardSnippet) {
        return TempBoardSnippetRedisHash.builder()
                .title(boardSnippet.getTitle())
                .content(boardSnippet.getContent())
                .thumbnailUrl(boardSnippet.getThumbnailUrl())
                .build();
    }

    // toDomain
    public BoardSnippet toDomain(){
        return BoardSnippet.of(this.getTitle(), this.getContent(), this.getThumbnailUrl());
    }

}
