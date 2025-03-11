package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.CategoryRequest;
import com.zip.community.platform.adapter.in.web.dto.response.CategoryResponse;
import com.zip.community.platform.application.port.in.board.CreateCategoryUseCase;
import com.zip.community.platform.application.port.in.board.GetCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final GetCategoryUseCase getService;
    private final CreateCategoryUseCase createService;

    // 카테고리 생성하기
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ApiResponse<CategoryResponse> save(@RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.created(CategoryResponse.from(createService.createCategory(categoryRequest)));
    }

    // 카테고리 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {

        return ApiResponse.created(CategoryResponse.from(getService.getByCategoryId(id)));
    }

    // 루트 카테고리 목록 조회하기
    @GetMapping("/list")
    public ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.ok(CategoryResponse.from(getService.getRootCategory()));
    }
}
