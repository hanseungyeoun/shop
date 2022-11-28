package com.example.shop.repositiory.order;

import com.example.shop.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderToken(String orderToken);


    Page<Order> findById(Long searchValue, Pageable pageable);

    Page<Order> findByUserNameContaining(String searchValue, Pageable pageable);


    Page<Order> findByOrderItemList_itemNameContaining(String searchValue, Pageable pageable);


    @Query(value = "select count(o) from Order o " +
            "where o.createdAt between :from and :to")
    Integer countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "select count(o) from Order o " +
            "where o.status = com.example.shop.domain.order.Order$Status.INIT " +
            "and o.createdAt between :from and :to ")
    Integer countByCreatedAtBetweenInit(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "select count(o) from Order o " +
            "where o.status = com.example.shop.domain.order.Order$Status.ORDER_COMPLETE " +
            "and o.createdAt between :from and :to ")
    Integer countByCreatedAtBetweenOrderComplete(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "select count(o) from Order o " +
            "where o.status = com.example.shop.domain.order.Order$Status.DELIVERY_PREPARE " +
            "and o.createdAt between :from and :to ")
    Integer countByCreatedAtBetweenDeliveryPrepare(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "select count(o) from Order o " +
        "where o.status = com.example.shop.domain.order.Order$Status.IN_DELIVERY " +
        "and o.createdAt between :from and :to ")
    Integer countByCreatedAtBetweenInDelivery(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "select count(o) from Order o " +
            "where o.status = com.example.shop.domain.order.Order$Status.DELIVERY_COMPLETE " +
            "and o.createdAt between :from and :to ")
    Integer countByCreatedAtBetweenDeliveryComplete(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
