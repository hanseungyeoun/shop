package com.example.shop.service.order;

import com.example.shop.domain.order.OrderStatistics;
import com.example.shop.dto.order.OrderStatisticsDto;
import com.example.shop.repositiory.order.OrderStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    final private OrderStatisticsRepository orderStatisticsRepository;

    public OrderStatisticsDto getOrderStatistics(LocalDate from, LocalDate to) {

        List<OrderStatistics> orderStatisticsList = orderStatisticsRepository.findByDateBetween(from, to);

        List<String> dates = orderStatisticsList.stream()
                .map(orderStatistics -> orderStatistics.getDate().toString())
                .collect(Collectors.toList());

        List<String> amounts = orderStatisticsList.stream()
                .map(orderStatistics -> orderStatistics.getAmount().toString())
                .collect(Collectors.toList());


        List<String> counts = orderStatisticsList.stream()
                .map(orderStatistics -> orderStatistics.getCount().toString())
                .collect(Collectors.toList());

        return OrderStatisticsDto.builder()
                .dates(dates)
                .totalSalesAmount(amounts)
                .totalSalesCount(counts)
                .build();
    }
}
