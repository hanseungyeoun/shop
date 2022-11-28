package com.example.shop.service.payment;

import com.example.shop.domain.order.Order;
import com.example.shop.dto.order.OrderDto;

public interface PaymentProcessor {
    void pay(Order order, OrderDto.PaymentRequest request);
}