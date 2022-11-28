package com.example.shop.dto.item.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategorySearchCondition {
    CategorySearchType searchType;
    String searchValue;
}
