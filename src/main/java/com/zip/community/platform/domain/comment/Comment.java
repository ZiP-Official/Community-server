package com.zip.community.platform.domain.comment;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@SuperBuilder
public class Comment extends BaseDomain {

    private String id;
    private Long boardId;
    private Long memberId;

    private String parentId;

    private CommentStatistics statistics;

    private String content;

    private boolean deleted;
    private boolean anonymous;

    // !- 글 작성자 여부를 보여주기 위한 도메인 | 엔티티에는 필요없다 .--!
    private boolean writer;

    // !- 댓글을 보여주기 위한 도메인 | 엔티티에는 필요없다 . --!
    private List<Comment> children;

    // !- 인기댓글을 보여주기 위한 도메인 | 엔티티에는 필요없다 . --!
    private boolean pinned;

    // 생성자
    public static Comment of(Long boardId, Long memberId, String parentId, String content, CommentStatistics statistics, boolean anonymous) {

        return Comment.builder()
                .boardId(boardId)
                .memberId(memberId)
                .parentId(parentId)
                .content(content)
                .statistics(statistics)
                .anonymous(anonymous)
                .writer(false)
                .pinned(false)
                .build();
    }

    public void changeWriter() {
        this.writer = true;
    }

    public void changeChildren(List<Comment> children) {
        this.children = children;
    }

    public void changeDeletedContent() {
        this.content = "삭제된 메시지 입니다.";
    }


}
