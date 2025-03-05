package com.zip.community.platform.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
public abstract class BaseDomain {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
