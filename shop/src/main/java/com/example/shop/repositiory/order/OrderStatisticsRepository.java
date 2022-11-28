package com.example.shop.repositiory.order;

import com.example.shop.domain.order.OrderStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderStatisticsRepository extends JpaRepository<OrderStatistics, Long> {

    List<OrderStatistics> findByDateBetween(LocalDate from, LocalDate to);
}
