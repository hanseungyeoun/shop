package com.example.shopbatch.domain.order.item;

import com.example.shopbatch.common.exception.InvalidParamException;
import com.example.shopbatch.domain.AbstractEntity;
import com.example.shopbatch.domain.order.Order;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer orderCount;
    private Long brandId;
    private Long itemId;
    private String itemName;
    private String itemToken;
    private Long itemPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.PERSIST)
    private List<OrderItemOptionGroup> orderItemOptionGroupList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    //==연관관계 메서드==//
    public void addOrderItemOptionGroupList(OrderItemOptionGroup orderItemOptionGroup) {
        orderItemOptionGroupList.add(orderItemOptionGroup);
        orderItemOptionGroup.setOrderItem(this);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Getter
    @AllArgsConstructor
    public enum DeliveryStatus {
        BEFORE_DELIVERY("배송전"),
        DELIVERY_PREPARE("배송준비중"),
        DELIVERING("배송중"),
        COMPLETE_DELIVERY("배송완료");

        private final String description;
    }

    public void changeOrder(Order order) {
        if(this.order != null) {
            this.order.getOrderItemList().remove(this);
        }

        this.order = order;
        order.getOrderItemList().add(this);
    }

    @Builder
    public OrderItem(
            Order order,
            Integer orderCount,
            Long brandId,
            Long itemId,
            String itemName,
            String itemToken,
            Long itemPrice
    ) {
        if (order == null) throw new InvalidParamException("OrderItemLine.order");
        if (orderCount == null) throw new InvalidParamException("OrderItemLine.orderCount");
        if (brandId == null) throw new InvalidParamException("OrderItemLine.partnerId");
        if (itemId == null && StringUtils.hasText(itemName))
            throw new InvalidParamException("OrderItemLine.itemNo and itemName");
        if (!StringUtils.hasText(itemToken)) throw new InvalidParamException("OrderItemLine.itemToken");
        if (itemPrice == null) throw new InvalidParamException("OrderItemLine.itemPrice");

        this.orderCount = orderCount;
        this.brandId = brandId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemToken = itemToken;
        this.itemPrice = itemPrice;
        this.deliveryStatus = DeliveryStatus.BEFORE_DELIVERY;

        changeOrder(order);
    }

    public Long calculateTotalAmount() {
        var itemOptionTotalAmount = orderItemOptionGroupList.stream()
                .mapToLong(OrderItemOptionGroup::calculateTotalAmount)
                .sum();
        return (itemPrice + itemOptionTotalAmount) * orderCount;
    }

    public String getOptionName(){
        return orderItemOptionGroupList.stream()
                .map(OrderItemOptionGroup::getOptionName)
                .collect(Collectors.joining(" < "));
    }

}
