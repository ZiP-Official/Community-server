package com.zip.community.platform.application.service.board;

import com.zip.community.common.exception.DuplicateCodeException;
import com.zip.community.platform.application.port.in.board.*;
import com.zip.community.platform.adapter.in.web.dto.request.board.CategoryRequest;
import com.zip.community.platform.application.port.out.board.CategoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.zip.community.platform.domain.board.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryService implements CreateCategoryUseCase, GetCategoryInfoUseCase {

    private final CategoryPort categoryPort;

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {

        // 부모가 없는 경우 null 처리
        Category parent = null;
        if (categoryRequest.getParentId() != null) {
            parent = categoryPort.loadCategory(categoryRequest.getParentId())
                    .orElseThrow(() -> new NoSuchElementException("부모 카테고리가 존재하지 않습니다."));
        }
        // code 중복 체크
        if (categoryRequest.getCode() != null && categoryPort.getCheckedExistCategory(categoryRequest.getCode())) {
            throw new DuplicateCodeException("이미 존재하는 코드입니다: " + categoryRequest.getCode());
        }

        // 카테고리 생성
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .code(categoryRequest.getCode())
                .parentId(categoryRequest.getParentId()) // 부모가 null일 수도 있음
                .build();

        // 카테고리 저장
        return categoryPort.saveCategory(category);
    }

    @Override
    public List<Category> getRootInfo() {
        return categoryPort.loadAllRootCategories();
    }

    @Override
    public Category getByCategoryId(Long categoryId) {
        return categoryPort.loadCategory(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지않습니다"));
    }


}
