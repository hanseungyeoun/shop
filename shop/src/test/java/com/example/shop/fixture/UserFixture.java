package com.example.shop.fixture;

import com.example.shop.domain.user.RoleType;
import com.example.shop.domain.user.UserAccount;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {
    public static UserAccount get(String userName, String password) {
        UserAccount entity = UserAccount.builder()
                .username(userName)
                .password(password)
                .role(RoleType.ROLE_USER)
                .build();

        ReflectionTestUtils.setField(entity, "id", 1L);
        return entity;
    }
}
