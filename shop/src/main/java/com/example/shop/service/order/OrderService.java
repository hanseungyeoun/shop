package com.example.shop.service.order;

import com.example.shop.common.constant.OrderSearchType;
import com.example.shop.common.exception.BaseException;
import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.common.exception.ErrorCode;
import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.item.Item;
import com.example.shop.domain.order.Order;
import com.example.shop.domain.order.item.OrderItem;
import com.example.shop.domain.order.item.OrderItemOption;
import com.example.shop.domain.order.item.OrderItemOptionGroup;
import com.example.shop.domain.user.UserAccount;
import com.example.shop.dto.order.OrderCountDto;
import com.example.shop.dto.user.User;
import com.example.shop.repositiory.item.ItemRepository;
import com.example.shop.repositiory.order.OrderRepository;
import com.example.shop.repositiory.order.item.OrderItemOptionGroupRepository;
import com.example.shop.repositiory.order.item.OrderItemOptionRepository;
import com.example.shop.repositiory.order.item.OrderItemRepository;
import com.example.shop.repositiory.user.UserRepository;
import com.example.shop.service.payment.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.example.shop.dto.order.OrderDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionGroupRepository orderItemOptionGroupRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final ItemRepository itemRepository;
    private final PaymentProcessor paymentProcessor;
    private final UserRepository userRepository;

    @Transactional
    public String registerItem(RegisterOrderRequest requestOrder, User user) {
        UserAccount loginUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("user를 찾을 수 없습니다"));

        UserAccount requestUser = userRepository.findById(requestOrder.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("user를 찾을 수 없습니다"));

        if(loginUser.getId() != requestUser.getId())
        {
            throw new IllegalArgumentException("잘못된 유저 입니다");
        }

        Order order = orderRepository.save(requestOrder.toEntity(requestUser.getUsername()));
        createOrderItemSeries(order, requestOrder);

        return order.getOrderToken();
    }

    @Transactional
    public void paymentOrder(PaymentRequest paymentRequest) {
        var orderToken = paymentRequest.getOrderToken();
        var order = orderRepository.findByOrderToken(orderToken)
                .orElseThrow(() -> new EntityNotFoundException("order 정보를 찾을 수 없습니다"));


        paymentProcessor.pay(order, paymentRequest);
        order.orderComplete();
    }

    public OrderResponse retrieveOrder(String orderToken) {
        Order order = orderRepository.findByOrderToken(orderToken)
                .orElseThrow(() -> new EntityNotFoundException("order 정보를 찾을 수 없습니다"));

        return new OrderResponse(order, getOrderItemSeries(order));
    }

    public Page<OrderResponse> searchOrders(OrderSearchType searchType, String searchValue, Pageable pageable) {
        try {
            Function<Order, OrderResponse> mapper = order -> new OrderResponse(order, getOrderItemSeries(order));

            if (searchValue == null || searchValue.isBlank()) {
                return orderRepository.findAll(pageable).map(mapper);
            }

            switch (searchType) {
                case ORDER_NO:
                    return orderRepository.findById(Long.parseLong(searchValue), pageable).map(mapper);

                case ORDER_NM:
                    return orderRepository.findByUserNameContaining(searchValue, pageable).map(mapper);

                case ITEM_NAME:
                    return orderRepository.findByOrderItemList_itemNameContaining(searchValue, pageable).map(mapper);
            }
            return Page.empty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidParamException("searchOrders");
        }
    }

    public OrderCountDto getOrderCount(LocalDateTime form, LocalDateTime to) {

        Integer totalCount = orderRepository.countByCreatedAtBetween(form, to);
        Integer initCount = orderRepository.countByCreatedAtBetweenInit(form, to);
        Integer orderCompleteCount = orderRepository.countByCreatedAtBetweenOrderComplete(form, to);
        Integer deliveryPrepareCount = orderRepository.countByCreatedAtBetweenDeliveryPrepare(form, to);
        Integer inDeliveryCount = orderRepository.countByCreatedAtBetweenInDelivery(form, to);
        Integer deliveryCompleteCount = orderRepository.countByCreatedAtBetweenDeliveryComplete(form, to);

        return OrderCountDto.builder()
                .orderTotalCount(totalCount)
                .orderInitCount(initCount)
                .orderComplete(orderCompleteCount)
                .orderDeliveryPrepareCount(deliveryPrepareCount)
                .orderInDeliveryCount(inDeliveryCount)
                .orderDeliveryComplete(deliveryCompleteCount)
                .build();
    }

    private List<OrderItem> createOrderItemSeries(Order order, RegisterOrderRequest requestOrder) {
        return requestOrder.getOrderItemList().stream()
                .map(orderItemRequest -> {
                    Item item = itemRepository.findByItemToken(orderItemRequest.getItemToken())
                            .orElseThrow(() -> new EntityNotFoundException("Item 정보를 찾을 수 없습니다"));

                    OrderItem initOrderItem = orderItemRequest.toEntity(order, item);
                    OrderItem orderItem = orderItemRepository.save(initOrderItem);

                    orderItemRequest.getOrderItemOptionGroupList().forEach(optionGroupRequest -> {
                        OrderItemOptionGroup initOrderItemOptionGroup = optionGroupRequest.toEntity(initOrderItem);
                        OrderItemOptionGroup orderItemOptionGroup = orderItemOptionGroupRepository.save(initOrderItemOptionGroup);

                        optionGroupRequest.getOrderItemOptionList().forEach(itemOptionRequest -> {
                            OrderItemOption initItemOption = itemOptionRequest.toEntity(orderItemOptionGroup);
                            orderItemOptionRepository.save(initItemOption);
                        });
                    });
                    return initOrderItem;
                }).collect(Collectors.toList());
    }

    private List<OrderItemResponse> getOrderItemSeries(Order order) {
        return order.getOrderItemList().stream()
                .map(orderItem -> {
                    List<OrderItemOptionGroupResponse> orderItemOptionGroupInfoList = orderItem.getOrderItemOptionGroupList().stream()
                            .map(orderItemOptionGroup -> {
                                List<OrderItemOptionResponse> orderItemOptionResponseList = orderItemOptionGroup.getOrderItemOptionList()
                                        .stream()
                                        .map(OrderItemOptionResponse::new)
                                        .collect(Collectors.toList());

                                return new OrderItemOptionGroupResponse(orderItemOptionGroup, orderItemOptionResponseList);
                            }).collect(Collectors.toList());

                    return new OrderItemResponse(orderItem, orderItemOptionGroupInfoList);
                }).collect(Collectors.toList());
    }


}
