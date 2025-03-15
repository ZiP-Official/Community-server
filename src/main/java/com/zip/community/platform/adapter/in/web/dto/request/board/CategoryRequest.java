package com.zip.community.platform.adapter.in.web.dto.request.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotEmpty(message = "Name은 비어있을 수 없습니다.")
    @Size(min = 2, max = 100, message = "Name은 2자 이상 100자 이하이어야 합니다.")
    private String name;

    @NotEmpty(message = "Code는 비어있을 수 없습니다.")
    @Size(min = 3, max = 50, message = "Code는 3자 이상 50자 이하이어야 합니다.")
    private String code;

    private Long parentId;
}
