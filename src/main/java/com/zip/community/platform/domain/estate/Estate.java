package com.zip.community.platform.domain.estate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Estate {

    private String id;
    private String kaptCode;
    private String kaptAddr;
    private String kaptName;

}
