package com.example.shop.controller.admin;

import com.example.shop.common.constant.OrderSearchType;
import com.example.shop.dto.order.OrderDto;
import com.example.shop.repositiory.item.ItemSearchCondition;
import com.example.shop.service.PaginationService;
import com.example.shop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/orders")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final PaginationService paginationService;

    @GetMapping
    public String orders(
            @ModelAttribute("orderSearch") OrderSearchCondition condition,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<OrderDto.OrderResponse> orders = orderService.searchOrders(condition.getSearchType(), condition.searchValue, pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), orders.getTotalPages());

        model.addAttribute("orders", orders);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchTypes", OrderSearchType.values());

        return "admin/orders/index";
    }
}
