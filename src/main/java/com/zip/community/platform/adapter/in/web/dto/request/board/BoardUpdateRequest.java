package com.zip.community.platform.adapter.in.web.dto.request.board;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardUpdateRequest {

    @NotNull(message = "boardId는 필수 입력값입니다.")
    private Long boardId;     // 수정할 게시글 ID

    @NotNull(message = "userId는 필수 입력값입니다.")
    private Long userId;      // 동일한 작성자인지 확인하기 위한 userId

    @NotNull(message = "categoryId는 필수 입력값입니다.")
    private Long categoryId;  // 수정할 카테고리

    // 수정할 게시물 내용
    private String title;
    private String content;

    // 수정할 사진 이미지
    private String thumbnailUrl;

}
