package com.zip.community.platform.adapter.in.web;

import lombok.Data;

@Data
public class CommentDeleteRequest {

    private String commentId;
    private Long userId;

}
