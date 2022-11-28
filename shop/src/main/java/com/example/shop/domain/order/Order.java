package com.example.shop.domain.order;

import com.example.shop.common.exception.IllegalStatusException;
import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.domain.order.fragment.DeliveryFragment;
import com.example.shop.domain.order.item.OrderItem;
import com.example.shop.util.TokenGenerator;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends AbstractEntity {

    private static final String ORDER_PREFIX = "ord_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderToken;
    private Long userId;
    private String userName;
    private String payMethod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Embedded
    private DeliveryFragment deliveryFragment;

    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        INIT("주문시작"),
        ORDER_COMPLETE("주문완료"),
        DELIVERY_PREPARE("배송준비"),
        IN_DELIVERY("배송중"),
        DELIVERY_COMPLETE("배송완료");

        private final String description;
    }

    @Builder
    public Order(
            Long userId,
            String userName,
            String payMethod,
            DeliveryFragment deliveryFragment
    ) {
        if (userId == null) throw new InvalidParamException("Order.userId");
        if (!StringUtils.hasText(userName)) throw new InvalidParamException("Order.userName");
        if (!StringUtils.hasText(payMethod)) throw new InvalidParamException("Order.payMethod");
        if (deliveryFragment == null) throw new InvalidParamException("Order.deliveryFragment");

        this.orderToken = TokenGenerator.randomCharacterWithPrefix(ORDER_PREFIX);
        this.userId = userId;
        this.userName = userName;
        this.payMethod = payMethod;
        this.deliveryFragment = deliveryFragment;
        this.orderedAt = LocalDateTime.now();
        this.status = Status.INIT;
    }

    public Long calculateTotalAmount() {
        return orderItemList.stream()
                .mapToLong(OrderItem::calculateTotalAmount)
                .sum();
    }

    public Long calculateTotalCount() {
        return Long.valueOf(orderItemList.size());
    }

    public void orderComplete() {
        if (this.status != Status.INIT) throw new IllegalStatusException();
        this.status = Status.ORDER_COMPLETE;
    }

    public boolean isAlreadyPaymentComplete() {
        switch (this.status) {
            case ORDER_COMPLETE:
            case DELIVERY_PREPARE:
            case IN_DELIVERY:
            case DELIVERY_COMPLETE:
                return true;
        }
        return false;
    }
}