package com.example.shop.repositiory.order.item;


import com.example.shop.domain.order.item.OrderItem;
import com.example.shop.repositiory.item.querydsl.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, ItemRepositoryCustom {
}
