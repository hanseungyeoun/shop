package com.example.shop.controller.admin;

import com.example.shop.common.constant.OrderSearchType;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class OrderSearchCondition {
    public OrderSearchType searchType;
    public String searchValue;
}
