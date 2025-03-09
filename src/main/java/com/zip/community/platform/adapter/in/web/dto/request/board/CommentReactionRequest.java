package com.zip.community.platform.adapter.in.web.dto.request.board;

import lombok.Data;

@Data
public class CommentReactionRequest {

    private Long id;

    private Long memberId;

    private String commentId;

}
