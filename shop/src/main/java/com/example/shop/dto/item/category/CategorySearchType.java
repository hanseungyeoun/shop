package com.example.shop.dto.item.category;

import lombok.Getter;

@Getter
public enum CategorySearchType {

    CATEGORY_TOKEN("토큰"),
    CATEGORY_NAME("카테고리명");

    private final String description;

    CategorySearchType(String description) {
        this.description = description;
    }
}
