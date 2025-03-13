package com.zip.community.platform.adapter.out.jpa.board;

import com.zip.community.platform.domain.board.BoardSnippet;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BoardSnippetJpaEntity {

    private String title;
    private String content;
    private String thumbnailUrl;

    // from
    public static BoardSnippetJpaEntity from(BoardSnippet boardSnippet) {
        return BoardSnippetJpaEntity.builder()
                .title(boardSnippet.getTitle())
                .content(boardSnippet.getContent())
                .thumbnailUrl(boardSnippet.getThumbnailUrl())
                .build();
    }

    // toDomain
    public BoardSnippet toDomain(){
        return BoardSnippet.of(this.getTitle(), this.getContent(), this.getThumbnailUrl());
    }

    // 비즈니스 로직
    public void update(BoardSnippet snippet) {
        this.title = snippet.getTitle();
        this.content = snippet.getContent();
        this.thumbnailUrl = snippet.getThumbnailUrl();
    }

}
