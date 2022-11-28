package com.example.shop.controller.api;

import com.example.shop.common.respose.CommonResponse;
import com.example.shop.dto.order.OrderDto;
import com.example.shop.dto.user.User;
import com.example.shop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.shop.dto.order.OrderDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class ApiOrderController {

    private final OrderService orderService;

    @PostMapping("/init")
    public CommonResponse registerOrder(
            @RequestBody @Valid RegisterOrderRequest request,
            @AuthenticationPrincipal User user
    ) {
        String orderToken = orderService.registerItem(request, user);
        RegisterOrderResponse response = new RegisterOrderResponse(orderToken);
        return CommonResponse.success(response);
    }

    @GetMapping("/{orderToken}")
    public CommonResponse retrieveOrder(@PathVariable String orderToken) {
        OrderResponse response = orderService.retrieveOrder(orderToken);
        return CommonResponse.success(response);
    }

    @PostMapping("/payment-order")
    public CommonResponse paymentOrder(@RequestBody @Valid OrderDto.PaymentRequest paymentRequest) {
        orderService.paymentOrder(paymentRequest);
        return CommonResponse.success("OK");
    }
}
