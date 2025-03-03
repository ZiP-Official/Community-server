package com.zip.community.platform.adapter.out.jpa.user;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.user.User;
import com.zip.community.platform.domain.user.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<UserRole> roles = new ArrayList<>();


    // from
    public static UserJpaEntity from(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
    }


    // toDomain
    public User toDomain() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .roles(roles)
                .build();
    }

}
