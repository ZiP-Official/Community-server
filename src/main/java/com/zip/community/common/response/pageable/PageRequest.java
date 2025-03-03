package com.zip.community.common.response.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PageRequest {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
