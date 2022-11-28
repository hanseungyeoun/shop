package com.example.shopbatch.common.constant;

import lombok.Getter;

@Getter
public enum OrderSearchType {
    ORDER_NO("주문번호"),
    ORDER_NM("주문자명"),
    ITEM_NAME("상품명");

    private final String description;

    OrderSearchType(String description) {
        this.description = description;
    }
}
