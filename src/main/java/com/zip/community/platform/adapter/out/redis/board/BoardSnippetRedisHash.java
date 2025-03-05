package com.zip.community.platform.adapter.out.redis.board;

import com.zip.community.platform.domain.board.BoardSnippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("boardSnippet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BoardSnippetRedisHash {

    private String title;
    private String content;
    private String thumbnailUrl;

    // from
    public static BoardSnippetRedisHash from(BoardSnippet boardSnippet) {
        return BoardSnippetRedisHash.builder()
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
