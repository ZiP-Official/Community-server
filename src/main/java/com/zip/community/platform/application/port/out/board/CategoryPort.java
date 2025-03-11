package com.zip.community.platform.application.port.out.board;


import com.zip.community.platform.domain.board.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryPort {

    /// Save 관련
    Category saveCategory(Category category);


    /// Load 관련
    List<Category> loadAllByCategoryId(List<Long> categoryIds);

    Optional<Category> loadCategory(Long categoryId);

    List<Category> loadAllRootCategories();

    List<Category> loadChildrenByParentId(Long parentId);

    boolean getCheckedExistCategory(String code);

    boolean getCheckedExistCategory(Long categoryId);

    /// Delete 관련
}
