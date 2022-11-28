package com.example.shop.repositiory.item;

import com.example.shop.domain.item.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageListRepository extends JpaRepository<ItemImage, Long> {

    void deleteAllByItem_ItemToken(String itemToken);
}
