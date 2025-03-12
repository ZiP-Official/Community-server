package com.zip.community.platform.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class BoardSnippet {

    private String title;
    private String content;
    private String thumbnailUrl;

    // 생성자
    public static BoardSnippet of(String title, String content,String thumbnailUrl) {
        return BoardSnippet.builder()
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .build();

    }

    // 수정
    // 직접 setter 메서드 작성
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
