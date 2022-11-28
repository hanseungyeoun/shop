package com.example.shop.service.payment.validator;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.order.Order;
import com.example.shop.service.payment.PaymentValidator;
import org.springframework.stereotype.Component;

import static com.example.shop.dto.order.OrderDto.*;

@org.springframework.core.annotation.Order(value = 2)
@Component
public class PayMethodValidator implements PaymentValidator {

    @Override
    public void validate(Order order, PaymentRequest paymentRequest) {
        if (!order.getPayMethod().equals(paymentRequest.getPayMethod().name())) {
            throw new InvalidParamException("주문 과정에서의 결제수단이 다릅니다.");
        }
    }
}
