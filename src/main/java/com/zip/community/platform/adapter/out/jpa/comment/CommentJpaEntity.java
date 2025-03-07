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


    // from
    public static CommentJpaEntity from(Comment comment) {

        return CommentJpaEntity.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .build();
    }

    // toDomain
    public static Comment toDomain(CommentJpaEntity entity){

        return Comment.builder()
                .id(entity.getId())
                .boardId(entity.getBoardId())
                .parentId(entity.getParentId())
                .memberId(entity.getMemberId())
                .content(entity.getContent())
                .build();
    }
}
