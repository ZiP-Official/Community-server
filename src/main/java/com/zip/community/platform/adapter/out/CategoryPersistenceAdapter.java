package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.board.CategoryJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.repository.CategoryJpaRepository;
import com.zip.community.platform.application.port.out.board.CategoryPort;
import com.zip.community.platform.domain.board.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPort {

    private final CategoryJpaRepository repository;

    @Override
    public Category saveCategory(Category category) {

        var entity = CategoryJpaEntity.from(category);
        return repository.save(entity).toDomain();
    }

    @Override
    public List<Category> loadAllByCategoryId(List<Long> categoryIds) {
        return repository.findAllById(categoryIds)
                .stream().map(CategoryJpaEntity::toDomain).toList();
    }

    @Override
    public Optional<Category> loadCategory(Long categoryId) {
        return repository.findById(categoryId)
                .map(CategoryJpaEntity::toDomain);
    }

    @Override
    public List<Category> loadAllRootCategories() {

        return repository.findRootCategories().stream()
                .map(CategoryJpaEntity::toDomain).toList();
    }

    @Override
    public boolean getCheckedExistCategory(String code) {
        return repository.existsByCode(code);
    }


}
