package com.zip.community.platform.domain.comment;

import com.zip.community.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment extends BaseDomain {

    private String id;
    private Long boardId;
    private Long memberId;
    private String parentId;

    private CommentStatistics statistics;

    private String content;

    // 생성자
    public static Comment of(Long boardId, Long memberId, String parentId, String content, CommentStatistics statistics) {

        return Comment.builder()
                .boardId(boardId)
                .memberId(memberId)
                .parentId(parentId)
                .content(content)
                .statistics(statistics)
                .build();

    }
}
