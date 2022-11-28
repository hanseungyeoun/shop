package com.example.shopbatch.repositiory.order.item;


import com.example.shopbatch.domain.order.item.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
