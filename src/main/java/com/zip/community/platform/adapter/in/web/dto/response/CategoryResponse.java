package com.zip.community.platform.adapter.in.web.dto.response;

import com.zip.community.platform.domain.board.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private List<CategoryResponse> children;

    // 단일 객체 변환
    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .children(from(category.getChildren()))
                .build();
    }

    // 리스트 변환
    public static List<CategoryResponse> from(List<Category> categories) {
        return categories == null ? List.of() : categories.stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }
}
