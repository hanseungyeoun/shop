package com.example.shop.repositiory.item;

import com.example.shop.domain.item.Item;
import com.example.shop.repositiory.item.querydsl.ItemRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository  extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    @Query(value = "select max(i.id) from Item i join i.brand b where b.id =:id")
    Optional<Long> findTopBrandIdByBrand_IdOrderByIdDesc(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"brand"})
    Optional<Item> findByItemToken(String itemToken);

    @Query(value = "select i from Item i join i.categoryItemList ci join ci.category c " +
            "where c.categoryToken =:categoryToken or c.parent.categoryToken =:categoryToken")
    Page<Item> findAllByCategoryToken(@Param(value = "categoryToken") String categoryToken, Pageable pageable);

    Page<Item> findAllByBrand_BrandToken(@Param(value = "brandToken") String brandToken, Pageable pageable);

    /*
    @Query(value = "select i from Item i where i.itemToken = :token")
    public Optional<Item> findFetchByItemToken(@Param(value = "token") String token);*/
}
