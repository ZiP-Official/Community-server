package com.zip.community.platform.application.port.in.board;


import com.zip.community.platform.adapter.in.web.dto.request.board.CategoryRequest;
import com.zip.community.platform.domain.board.Category;

public interface CreateCategoryUseCase {

    // 새로운 카테고리를 추가
    Category createCategory(CategoryRequest categoryRequest);


}
