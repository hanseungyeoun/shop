package com.example.shop.service.payment;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.shop.dto.order.OrderDto.*;

@RequiredArgsConstructor
@Component
public class PaymentProcessorImpl implements PaymentProcessor{

    private final List<PaymentValidator> paymentValidatorList;
    private final List<PaymentApiCaller> paymentApiCallerList;

    @Override
    public void pay(Order order, PaymentRequest request) {
        paymentValidatorList.forEach(paymentValidator -> {
            paymentValidator.validate(order, request);
        });
        routingApiCaller(request).pay(request);
    }

    private PaymentApiCaller routingApiCaller(PaymentRequest request) {
        return paymentApiCallerList.stream()
                .filter(paymentApiCaller -> paymentApiCaller.support(request.getPayMethod()))
                .findFirst()
                .orElseThrow(InvalidParamException::new);
    }
}
