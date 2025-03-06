package com.zip.community.platform.adapter.in.web.dto.request.board;

import lombok.Data;

@Data
public class BoardReactionRequest {

    private Long memberId;

    private Long boardId;
}
