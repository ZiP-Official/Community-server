package com.zip.community.platform.application.port.out.board;


import com.zip.community.platform.domain.board.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryPort {

    Category saveCategory(Category category);

    List<Category> loadAllByCategoryId(List<Long> categoryIds);

    Optional<Category> loadCategory(Long categoryId);

    List<Category> loadAllRootCategories();

    boolean getCheckedExistCategory(String code);

}
