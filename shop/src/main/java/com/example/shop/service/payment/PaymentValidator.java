package com.example.shop.service.payment;

import com.example.shop.domain.order.Order;

import static com.example.shop.dto.order.OrderDto.*;

public interface PaymentValidator {
    void validate(Order order, PaymentRequest paymentRequest);
}
