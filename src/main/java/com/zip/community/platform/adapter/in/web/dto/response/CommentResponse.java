package com.zip.community.platform.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zip.community.platform.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@Builder
public class CommentResponse {

    private String id;
    private String content;
    private Long author;

    private boolean isWriter;

    private long likeCount;
    private long dislikeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<CommentResponse> replies = new ArrayList<>();

    // from
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getMemberId())
                .isWriter(comment.isWriter())
                .likeCount(comment.getStatistics().getLikeCount())
                .dislikeCount(comment.getStatistics().getDislikeCount())
                .replies(CommentResponse.from(comment.getChildren()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    // 리스트 변환
    public static List<CommentResponse> from(List<Comment> comments) {
        // null 처리 추가: null이 들어올 경우 빈 리스트 반환

        if (comments == null) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }
}
