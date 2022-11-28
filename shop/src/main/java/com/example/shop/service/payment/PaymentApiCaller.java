package com.example.shop.service.payment;

import com.example.shop.dto.order.OrderDto;

public interface PaymentApiCaller {
    boolean support(PayMethod payMethod);
    void pay(OrderDto.PaymentRequest request);
}