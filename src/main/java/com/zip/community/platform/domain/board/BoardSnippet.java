package com.zip.community.platform.domain.board;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardSnippet {

    private String title;
    private String content;
    private String description;
    private String thumbnailUrl;

    // 생성자
    public static BoardSnippet of(String title, String content,String thumbnailUrl) {
        return BoardSnippet.builder()
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .build();

    }

}
