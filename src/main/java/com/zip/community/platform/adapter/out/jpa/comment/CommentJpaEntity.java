package com.zip.community.platform.adapter.out.jpa.comment;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentJpaEntity extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    private String id;

    private Long boardId;

    private Long memberId;

    // 대댓글을 위한 설정
    private String parentId;

    private String content;

    @Embedded
    private CommentStatisticsJpaEntity statistics;


    // from
    public static CommentJpaEntity from(Comment comment) {

        return CommentJpaEntity.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .statistics(CommentStatisticsJpaEntity.from(comment.getStatistics()))
                .build();
    }

    // toDomain
    public Comment toDomain(){

        return Comment.builder()
                .id(id)
                .boardId(boardId)
                .parentId(parentId)
                .memberId(memberId)
                .content(content)
                .statistics(statistics.toDomain())
                .createdAt(this.getCreated())
                .updatedAt(this.getUpdated())
                .build();
    }

}
