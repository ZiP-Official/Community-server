package com.zip.community.platform.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.zip.community.platform.domain.board.Board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponse {

    private Long category; // Immutable 빈 리스트 사용

    private Long id;
    private Long author;
    private String title;
    private String content;

    @Builder.Default
    private String thumbnailUrl = "";

    private long viewCount;
    private long commentCount;
    private long likeCount;  // Long → long으로 통일

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    // 생성자
    public static BoardResponse from(Board board) {

        return BoardResponse.builder()
                .category(board.getCategoryId())
                .id(board.getId())
                .title(board.getSnippet().getTitle())
                .content(board.getSnippet().getContent())
                .author(board.getMemberId())
                .viewCount(board.getStatistics().getViewCount())
                .commentCount(board.getStatistics().getCommentCount())
                .likeCount(board.getStatistics().getLikeCount())
                .build();
    }

    // 리스트 변환
    public static List<BoardResponse> from(List<Board> boards) {
        return boards.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
    }


}
