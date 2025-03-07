package com.zip.community.platform.adapter.in.web.dto.request.board;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {

    // 작성자
    private Long memberId;

    // 작성 하고자 하는 게시판
    private Long boardId;

    // 대댓글을 위한 아이디
    private String parentId;

    // 작성하는 댓글
    private String content;
}
