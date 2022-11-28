package com.example.shop.repositiory.item.category;

import com.example.shop.domain.item.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Optional<Category> findByCategoryToken(String token);

    @Query(value = "select c from Category c " +
            "where c.level = com.example.shop.domain.item.category.Category$Level.PRIMARY_CATEGORY")
    public List<Category> findAllPrimaryCategory();

    public List<Category> findByParent_CategoryToken(@Param(value = "token") String token);

    public List<Category> findByParent_CategoryName(@Param(value = "name") String name);

    List<Category> findByCategoryItemList_Item_itemToken(String itemToken);

    @Modifying
    void deleteByCategoryToken(String itemToken);
}
