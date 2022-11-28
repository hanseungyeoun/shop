package com.example.shop.controller.admin;

import com.example.shop.dto.order.OrderCountDto;
import com.example.shop.dto.order.OrderStatisticsDto;
import com.example.shop.repositiory.order.OrderRepository;
import com.example.shop.service.order.OrderService;
import com.example.shop.service.order.OrderStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class HomeController {

    private final OrderService orderService;
    private final OrderStatisticsService orderStatisticsService;

    @GetMapping
    public String home(Model model) {
        OrderCountDto orderCount = orderService.getOrderCount(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        OrderStatisticsDto orderStatistics = orderStatisticsService.getOrderStatistics(LocalDate.now().minusDays(7), LocalDate.now());
        System.out.println("orderCount = " + orderCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("orderStatistics", orderStatistics);
        return "admin/home/home";
    }
}
