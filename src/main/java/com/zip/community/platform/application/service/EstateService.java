package com.zip.community.platform.application.service;

import com.zip.community.platform.application.port.in.estate.GetEstateUseCase;
import com.zip.community.platform.application.port.out.estate.LoadEstatePort;
import com.zip.community.platform.domain.estate.Estate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstateService implements GetEstateUseCase {

    private final LoadEstatePort loadPort;

    @Override
    public Optional<Estate> loadEstateByCode(String kaptCode) {
        return loadPort.loadEstateByCode(kaptCode);
    }

    @Override
    public Optional<Estate> loadEstateByName(String kaptName) {
        return loadPort.loadEstateByName(kaptName);
    }

}
