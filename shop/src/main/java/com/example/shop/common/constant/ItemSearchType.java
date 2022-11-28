package com.example.shop.common.constant;

import lombok.Getter;

@Getter
public enum ItemSearchType {
    ITEM_NAME("상품명"),
    BRAND_NAME("브랜드 명"),
    ITEM_CODE("상품 코드"),
    ITEM_ID("상품 ID");

    private final String description;

    ItemSearchType(String description) {
        this.description = description;
    }
}
