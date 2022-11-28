package com.example.shop.repositiory.item;

import com.example.shop.domain.item.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

    Optional<ItemOption> findByItemOptionName(String name);

    void deleteAllByItemOptionGroup_Item_itemToken(String itemToken);
}
