package com.zip.community.platform.adapter.out.jpa;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)  // 생성일은 수정되지 않음
    private LocalDateTime created; // 생성일 자동 설정

    @LastModifiedDate
    @Column(nullable = false)  // 수정일은 변경 가능
    private LocalDateTime updated; // 수정일 자동 설정
}
