package com.zip.community.platform.domain.board;

import com.zip.community.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class BoardSnippet extends BaseDomain {

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
