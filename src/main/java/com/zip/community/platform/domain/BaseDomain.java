package com.zip.community.platform.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public abstract class BaseDomain {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
