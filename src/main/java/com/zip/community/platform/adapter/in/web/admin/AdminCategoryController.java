package com.zip.community.platform.adapter.in.web.admin;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.CategoryRequest;
import com.zip.community.platform.adapter.in.web.dto.response.CategoryResponse;
import com.zip.community.platform.application.port.in.board.CreateCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CreateCategoryUseCase createService;

    // 카테고리 추가하기
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/category")
    public ApiResponse<CategoryResponse> save(@RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.created(CategoryResponse.from(createService.createCategory(categoryRequest)));
    }

}
