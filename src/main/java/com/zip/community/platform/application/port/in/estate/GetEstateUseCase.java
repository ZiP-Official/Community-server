package com.zip.community.platform.application.port.in.estate;

import com.zip.community.platform.domain.estate.Estate;

import java.util.Optional;

public interface GetEstateUseCase {

    /*
        아파트 정보 가져오는 유즈케이스
     */

    // 코드를 바탕으로 아파트 가져오기
    Optional<Estate> loadEstateByCode(String kaptCode);

    // 이름을 바탕으로 아파트 가져오기
    Optional<Estate> loadEstateByName(String kaptName);



}
