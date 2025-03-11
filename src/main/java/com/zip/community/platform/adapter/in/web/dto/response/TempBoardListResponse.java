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
public class TempBoardListResponse {

    private Long category; // Immutable 빈 리스트 사용

    private Long author;
    private String title;

    @Builder.Default
    private String thumbnailUrl = "";


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    // 생성자
    public static TempBoardListResponse from(Board board) {

        return TempBoardListResponse.builder()
                .category(board.getCategoryId())
                .title(board.getSnippet().getTitle())
                .author(board.getMemberId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    // List 해결
    public static List<TempBoardListResponse> from(List<Board> boards) {
        return boards.stream()
                .map(TempBoardListResponse::from)
                .collect(Collectors.toList());
    }

}
