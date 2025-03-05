package com.zip.community.platform.adapter.out.jpa.board;

import com.zip.community.platform.domain.board.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;


    private Long parentId;

    // from
    public static CategoryJpaEntity from(Category category) {

        return CategoryJpaEntity.builder()
                .name(category.getName())
                .code(category.getCode())
                .parentId(category.getParentId())  // 부모 변환
                .build();
    }

    // toDomain
    public Category toDomain() {
        return Category.builder()
                .id(id)
                .name(name)
                .code(code)
                .parentId(parentId)
                .build();
    }
}
