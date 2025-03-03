package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.response.CategoryResponse;
import com.zip.community.platform.application.port.in.board.GetCategoryInfoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final GetCategoryInfoUseCase getService;

    // 카테고리 상세 조회
    @GetMapping("/category/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {

        return ApiResponse.created(CategoryResponse.from(getService.getByCategoryId(id)));
    }
}
