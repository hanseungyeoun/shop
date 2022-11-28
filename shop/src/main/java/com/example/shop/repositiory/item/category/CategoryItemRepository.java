package com.example.shop.repositiory.item.category;



import com.example.shop.domain.item.category.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.example.shop.domain.item.category.Category.*;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {

    @Query(value =
            "select " +
            "   ci " +
            "from " +
            "   CategoryItem ci " +
            "join " +
            "   ci.item i " +
            "join " +
            "   ci.category c " +
            "where " +
            "   i.itemToken = :itemToken " +
            "   and c.level = :level")
    List<CategoryItem> findByItemTokenAndCategory_Level(
            @Param(value ="itemToken") String itemToken,
            @Param(value = "level") Level level
    );
}
