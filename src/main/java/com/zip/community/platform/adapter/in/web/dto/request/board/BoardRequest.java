package com.zip.community.platform.adapter.in.web.dto.request.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class BoardRequest {

    // 카테고리 (null이 아니어야 함)
    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    // 게시물 작성자 ID (null이 아니어야 함)
    @NotNull(message = "작성자 ID는 필수입니다.")
    private Long memberId;

    // 게시글 제목 (비어있지 않아야 함, 최소 1자 이상, 최대 100자 이하)
    @NotEmpty(message = "제목은 필수입니다.")
    @Size(min = 3, max = 100, message = "제목은 1자 이상 100자 이하로 입력해야 합니다.")
    private String title;

    // 게시글 내용 (비어있지 않아야 함, 최소 10자 이상, 최대 1000자 이하)
    @NotEmpty(message = "내용은 필수입니다.")
    @Size(min = 10, max = 1000, message = "내용은 10자 이상 1000자 이하로 입력해야 합니다.")
    private String content;
}
