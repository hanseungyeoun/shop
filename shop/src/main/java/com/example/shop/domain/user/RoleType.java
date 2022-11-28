package com.example.shop.domain.user;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_ADMIN("Admin"),
    ROLE_USER("User");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }
}