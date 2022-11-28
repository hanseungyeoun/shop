package com.example.shop.service.payment.validator;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.order.Order;
import com.example.shop.dto.order.OrderDto;
import com.example.shop.service.payment.PaymentValidator;
import org.springframework.stereotype.Component;

@org.springframework.core.annotation.Order(value = 3)
@Component
public class PayStatusValidator implements PaymentValidator {

    @Override
    public void validate(Order order, OrderDto.PaymentRequest paymentRequest) {
        if (order.isAlreadyPaymentComplete()) throw new InvalidParamException("이미 결제완료된 주문입니다");
    }
}
