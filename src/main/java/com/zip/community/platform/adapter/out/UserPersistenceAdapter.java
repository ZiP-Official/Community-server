package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.user.UserJpaEntity;
import com.zip.community.platform.adapter.out.jpa.user.UserJpaEntityRepository;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort {

    private final UserJpaEntityRepository repository;

    @Override
    public Optional<User> loadUser(Long id) {
        return repository.findById(id)
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean getCheckedExistUser (Long memberId) {
        return repository.existsById(memberId);
    }

}
