package com.example.shop.service.payment.validator;


import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.order.Order;
import com.example.shop.dto.order.OrderDto;
import com.example.shop.service.payment.PaymentValidator;
import org.springframework.stereotype.Component;

@org.springframework.core.annotation.Order(value = 1)
@Component
public class PayAmountValidator implements PaymentValidator {

    @Override
    public void validate(Order order, OrderDto.PaymentRequest paymentRequest) {
        if (!order.calculateTotalAmount().equals(paymentRequest.getAmount()))
            throw new InvalidParamException("주문가격이 불일치합니다");
    }
}
