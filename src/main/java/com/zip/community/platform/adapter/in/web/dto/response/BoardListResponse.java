package com.zip.community.platform.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zip.community.platform.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponse {

    private Long category; // Immutable 빈 리스트 사용

    private Long id;
    private Long author;
    private String title;

    @Builder.Default
    private String thumbnailUrl = "";

    private long commentCount;
    private long likeCount;  // Long → long으로 통일

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    // 생성자
    public static BoardListResponse from(Board board) {

        return BoardListResponse.builder()
                .category(board.getCategoryId())
                .id(board.getId())
                .title(board.getSnippet().getTitle())
                .author(board.getMemberId())
                .commentCount(board.getStatistics().getCommentCount())
                .likeCount(board.getStatistics().getLikeCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    // List 해결
    public static List<BoardListResponse> from(List<Board> boards) {
        return boards.stream()
                .map(BoardListResponse::from)
                .collect(Collectors.toList());
    }

}
