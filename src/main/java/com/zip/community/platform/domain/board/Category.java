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
    private List<Category> children;

    // 생성자
    public static Category of(Long parentId,String name, String code) {

        return Category.builder()
                .name(name)
                .code(code)
                .parentId(parentId)
                .build();
    }

    public void changeChildren(List<Category> children) {
        this.children = children;
    }

}
