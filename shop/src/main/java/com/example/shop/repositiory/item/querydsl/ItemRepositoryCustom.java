package com.example.shop.repositiory.item.querydsl;

import com.example.shop.domain.item.Item;
import com.example.shop.repositiory.item.ItemSearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findAll(ItemSearchCondition condition, Pageable pageable);
    Integer itemTotalCount(ItemSearchCondition condition);
}