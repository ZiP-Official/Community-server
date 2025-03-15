package com.zip.community.platform.adapter.in.web.dto.request.board;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {

    // 작성자
    @NotNull(message = "작성자 ID는 필수입니다.")
    private Long memberId;

    // 작성 하고자 하는 게시판
    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long boardId;

    // 대댓글을 위한 아이디
    private String parentId;

    // 작성하는 댓글
    @NotNull(message = "댓글 내용은 필수입니다.")
    private String content;

    private boolean anonymous;
}
