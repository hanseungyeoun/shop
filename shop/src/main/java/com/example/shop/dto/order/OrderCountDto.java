package com.example.shop.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCountDto {

    private Integer orderTotalCount;
    private Integer orderInitCount;
    private Integer orderComplete;
    private Integer orderDeliveryPrepareCount;
    private Integer orderInDeliveryCount;
    private Integer orderDeliveryComplete;
}
