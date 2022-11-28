package com.example.shop.domain.user;

import com.example.shop.common.exception.InvalidParamException;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;


@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString

public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    @Builder
    private UserAccount(
            String username,
            String password,
            RoleType role
    ) {
        if(!StringUtils.hasText(username)) throw new InvalidParamException("UserAccount.username");
        if(!StringUtils.hasText(password)) throw new InvalidParamException("UserAccount.password");

        this.username = username;
        this.password = password;
        this.role = role;
    }
}
