package com.zip.community.platform.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private String code;
    private Long parentId;

    // 생성자
    public static Category of(Long id, Long parentId,String name, String code) {

        return Category.builder()
                .id(id)
                .name(name)
                .code(code)
                .parentId(parentId)
                .build();
    }

}
