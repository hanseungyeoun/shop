package com.example.shopbatch.domain.order.item;

import com.example.shopbatch.common.exception.InvalidParamException;
import com.example.shopbatch.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item_option_group")
public class OrderItemOptionGroup extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    private Integer ordering;
    private String itemOptionGroupName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItemOptionGroup", cascade = CascadeType.PERSIST)
    private List<OrderItemOption> orderItemOptionList = new ArrayList<>();

    //==연관관계 메서드==//
    public void changeOrderItem(OrderItem orderItem) {
        if(this.orderItem != null) {
            this.orderItem.getOrderItemOptionGroupList().remove(this);
        }

        this.orderItem = orderItem;
        orderItem.getOrderItemOptionGroupList().add(this);
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Builder
    public OrderItemOptionGroup(
            OrderItem orderItem,
            Integer ordering,
            String itemOptionGroupName
    ) {
        if (orderItem == null) throw new InvalidParamException("OrderItemLine.orderItemOptionGroup.orderItem");
        if (ordering == null) throw new InvalidParamException("OrderItemLine.orderItemOptionGroup.ordering");
        if (!StringUtils.hasText(itemOptionGroupName)) throw new InvalidParamException("OrderItemLine.orderItemOptionGroup.itemOptionGroupName");

        this.ordering = ordering;
        this.itemOptionGroupName = itemOptionGroupName;

        changeOrderItem(orderItem);
    }

    public Long calculateTotalAmount() {
        return orderItemOptionList.stream()
                .mapToLong(OrderItemOption::getItemOptionPrice)
                .sum();
    }

    public String getOptionName() {
        return orderItemOptionList.stream()
                .map(OrderItemOption::getItemOptionName)
                .collect(Collectors.joining(" < "));
    }
}