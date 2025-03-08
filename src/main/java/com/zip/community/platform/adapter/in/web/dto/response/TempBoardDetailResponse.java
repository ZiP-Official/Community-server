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
public class TempBoardDetailResponse {

    private Long category;

    private Long author;

    private String title;
    private String content;

    @Builder.Default
    private String thumbnailUrl = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    // 생성자
    public static TempBoardDetailResponse from(Board board) {

        return TempBoardDetailResponse.builder()
                .category(board.getCategoryId())
                .title(board.getSnippet().getTitle())
                .content(board.getSnippet().getContent())
                .author(board.getMemberId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    // 리스트 변환
    public static List<TempBoardDetailResponse> from(List<Board> boards) {
        return boards.stream()
                .map(TempBoardDetailResponse::from)
                .collect(Collectors.toList());
    }


}
