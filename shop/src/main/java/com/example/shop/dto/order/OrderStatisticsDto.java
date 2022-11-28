package com.example.shop.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderStatisticsDto {

    private List<String> totalSalesAmount;
    private List<String> totalSalesCount;
    private List<String> dates;
}
