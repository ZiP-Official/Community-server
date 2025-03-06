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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CreateCategoryUseCase, GetCategoryUseCase {

    private final CategoryPort categoryPort;

    /// 생성 서비스
    @Override
    public Category createCategory(CategoryRequest request) {

        // 부모가 없는 경우, root 카테고리
        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryPort.loadCategory(request.getParentId())
                    .orElseThrow(() -> new NoSuchElementException("부모 카테고리가 존재하지 않습니다."));
        }

        // code 중복 체크
        if (request.getCode() != null && categoryPort.getCheckedExistCategory(request.getCode())) {
            throw new DuplicateCodeException("이미 존재하는 코드입니다: " + request.getCode());
        }

        // 카테고리 생성
        Category category = Category.of(request.getParentId(), request.getName(), request.getCode());

        // 카테고리 저장
        return categoryPort.saveCategory(category);
    }

    /// 조회 서비스
    // 루트 목록 조회
    @Override
    public List<Category> getRootCategory() {

        // 루트 목록 가져오기
        List<Category> categories = categoryPort.loadAllRootCategories();

        // children으로 엮기
        categories.forEach(
                category -> {
                    List<Category> children = categoryPort.loadChildrenByParentId(category.getId());
                    category.changeChildren(children);
                }
        );
        return categories;
    }


    // 카테고리 상세 조회
    @Override
    public Category getByCategoryId(Long categoryId) {
        // 카테고리 조회
        Optional<Category> optionalCategory = categoryPort.loadCategory(categoryId);

        // 카테고리가 존재하면 자식 카테고리 설정 후 반환
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();  // 카테고리 가져오기
            List<Category> children = getChildCategoryByParentId(category.getId());  // 자식 카테고리 조회

            category.changeChildren(children);  // 자식 카테고리 설정
            return category;  // 설정된 카테고리 반환
        }

        // 카테고리가 없으면 null 반환 (예외 처리 고려 가능)
        throw new EntityNotFoundException("해당하는 엔티티가 존재하지 않습니다.");
    }


    /// 내부 함수
    private List<Category> getChildCategoryByParentId(Long parentId) {
        return categoryPort.loadChildrenByParentId(parentId);
    }

}
