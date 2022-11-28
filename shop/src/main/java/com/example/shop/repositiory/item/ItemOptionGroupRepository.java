package com.example.shop.repositiory.item;

import com.example.shop.domain.item.ItemOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemOptionGroupRepository extends JpaRepository<ItemOptionGroup, Long> {
    Optional<ItemOptionGroup> findByItemOptionGroupName(String name);

    void deleteAllByItem_ItemToken(String itemToken);
}
