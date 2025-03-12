package com.zip.community.platform.adapter.in.web.dto.request.board;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardUpdateRequest {

    // 수정할 게시글 아이디
    private Long boardId;

    // 동일한 작성자인지 확인하기위한 userId
    private Long userId;

    // 수정할 카테고리
    private Long categoryId;

    // 수정할 게시물 내용
    private String title;
    private String content;

    // 수정할 사진 이미지
    private String thumbnailUrl;

}
