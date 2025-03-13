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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long boardId;

    private Long memberId;

    // 대댓글을 위한 설정
    private String parentId;

    private String content;

    @Embedded
    private CommentStatisticsJpaEntity statistics;

    private boolean deleted;
    private boolean anonymous;

    // from
    public static CommentJpaEntity from(Comment comment) {

        return CommentJpaEntity.builder()
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .statistics(CommentStatisticsJpaEntity.from(comment.getStatistics()))
                .anonymous(comment.isAnonymous())
                .build();
    }

    // toDomain
    public Comment toDomain(){

        return Comment.builder()
                .id(String.valueOf(id))
                .boardId(boardId)
                .parentId(parentId)
                .memberId(memberId)
                .content(content)
                .statistics(statistics.toDomain())
                .anonymous(anonymous)
                .createdAt(this.getCreated())
                .updatedAt(this.getUpdated())
                .build();
    }

    /// 비즈니스 로직
    public void delete(){
        this.deleted = true;
    }

}
