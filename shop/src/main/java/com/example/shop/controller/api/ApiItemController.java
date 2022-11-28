package com.example.shop.controller.api;

import com.example.shop.common.respose.CommonResponse;
import com.example.shop.dto.item.ItemDto;
import com.example.shop.service.CategoryService;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.shop.dto.item.ItemDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ApiItemController {

    private final ItemService service;

    @GetMapping("/categories/{categoryToken}")
    CommonResponse searchItemByCategoryToken(
        @PathVariable String categoryToken,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ItemResponse> itemResponses = service.searchItemByCategoryToken(categoryToken, pageable);
        return CommonResponse.success(itemResponses);
    }

    @GetMapping("/brands/{brandToken}")
    CommonResponse searchItemByBrandToken(
            @PathVariable String brandToken,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ItemResponse> itemResponses = service.searchItemByBrandToken(brandToken, pageable);
        return CommonResponse.success(itemResponses);
    }

    @GetMapping("/{itemToken}")
    CommonResponse getItemByItemToken(
            @PathVariable String itemToken
    ) {
        ItemResponse itemResponses = service.getItemByItemToken(itemToken);
        return CommonResponse.success(itemResponses);
    }
}