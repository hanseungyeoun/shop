package com.example.shop.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.shop.dto.order.OrderDto.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverPayApiCaller implements PaymentApiCaller {

    @Override
    public boolean support(PayMethod payMethod) {
        return PayMethod.NAVER_PAY == payMethod;
    }

    @Override
    public void pay(PaymentRequest request) {
        // TODO - 구현
    }
}
