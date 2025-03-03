package com.zip.community.platform.adapter.in.web.dto.request.board;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String code;

    private Long parentId;


}
