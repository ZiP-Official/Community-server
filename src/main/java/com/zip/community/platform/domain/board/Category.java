package com.zip.community.platform.domain.board;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class Category extends BaseDomain {

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
