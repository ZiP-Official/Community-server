package com.zip.community.platform.domain.user;

import com.zip.community.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User extends BaseDomain {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private List<UserRole> roles;

    public static User of(String username, String email, String password,
                          String phoneNumber) {

        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.FIRST);

        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .roles(roles)
                .build();
    }


}

