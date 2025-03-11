package com.zip.community.platform.application.port.out.user;


import com.zip.community.platform.domain.user.User;

import java.util.Optional;

public interface LoadUserPort {

    Optional<User> loadUser(Long id);

    boolean getCheckedExistUser(Long memberId);
}

