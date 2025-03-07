package com.zip.community.platform.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zip.community.platform.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private String id;
    private String content;
    private Long author;

    @Builder.Default
    private Boolean isWriter = false;

    private long likeCount;
    private long dislikeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<CommentResponse> children = new ArrayList<>();

    // 부모-자식 관계를 재귀적으로 변환하는 메서드
    public static CommentResponse from(Comment comment, Long writerId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getMemberId())
                .isWriter(Objects.equals(comment.getMemberId(), writerId))
                .likeCount(comment.getStatistics().getLikeCount())
                .dislikeCount(comment.getStatistics().getDislikeCount())
                .build();
    }

    // 리스트 변환 (재귀적으로 호출)
    public static List<CommentResponse> from(List<Comment> comments, Long writerId) {
        if (comments == null) {
            return Collections.emptyList();
        }
        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : comments) {
            responses.add(from(comment, writerId)); // 재귀적으로 호출하여 변환
        }
        return responses;
    }
}
