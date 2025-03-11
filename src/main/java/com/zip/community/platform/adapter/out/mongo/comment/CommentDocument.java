package com.zip.community.platform.adapter.out.mongo.comment;

import com.zip.community.platform.domain.comment.Comment;
import com.zip.community.platform.domain.comment.CommentStatistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDocument {


    @Field("_id")
    private String id;

    // 게시글 ID
    private Long boardId;

    // 유저 ID
    private Long userId;

    private String content;

    // 대댓글을 위한
    private String parentId;

    private Long likeCount;

    private Long dislikeCount;

    public static CommentDocument from(Comment comment) {

        return CommentDocument.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .userId(comment.getMemberId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(0L)
                .dislikeCount(0L)
                .build();
    }

    public Comment toDomain() {

        CommentStatistics statistics = CommentStatistics.builder()
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .build();

        return Comment.builder()
                .id(id)
                .boardId(boardId)
                .memberId(userId)
                .content(content)
                .parentId(parentId)
                .statistics(statistics)
                .build();
    }

}
