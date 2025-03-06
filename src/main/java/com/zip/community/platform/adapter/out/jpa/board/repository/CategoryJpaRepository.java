package com.zip.community.platform.adapter.out.jpa.board.repository;

import com.zip.community.platform.adapter.out.jpa.board.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    // 최상위 계층 카테고리 검색하기
    @Query("SELECT bc FROM CategoryJpaEntity bc WHERE bc.parentId IS NULL")
    List<CategoryJpaEntity> findRootCategories();

    // 존재하는지
    boolean existsByCode(String code);

    // 특정 부모가 있는 카테고리 검색하기
    List<CategoryJpaEntity> findByParentId(Long parentId);
}
