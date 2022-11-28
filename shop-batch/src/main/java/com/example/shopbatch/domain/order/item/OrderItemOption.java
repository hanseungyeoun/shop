package com.example.shopbatch.domain.order.item;

import com.example.shopbatch.common.exception.InvalidParamException;
import com.example.shopbatch.domain.AbstractEntity;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item_option")
public class OrderItemOption extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_option_group_id")
    private OrderItemOptionGroup orderItemOptionGroup;
    private Integer ordering;
    private String itemOptionName;
    private Long itemOptionPrice;

    //==연관관계 메서드==//
    public void changeOrderItemOptionGroup(OrderItemOptionGroup group) {
        if(this.orderItemOptionGroup != null) {
            this.orderItemOptionGroup.getOrderItemOptionList().remove(this);
        }

        this.orderItemOptionGroup = group;
        group.getOrderItemOptionList().add(this);
    }

    @Builder
    public OrderItemOption(
            OrderItemOptionGroup orderItemOptionGroup,
            Integer ordering,
            String itemOptionName,
            Long itemOptionPrice
    ) {
        if (orderItemOptionGroup == null) throw new InvalidParamException("OrderItemLine.OrderItemOption.orderItemOptionGroup");
        if (ordering == null) throw new InvalidParamException("OrderItemLine.OrderItemOption.ordering");
        if (!StringUtils.hasText(itemOptionName)) throw new InvalidParamException("OrderItemLine.OrderItemOption.itemOptionName");
        if (itemOptionPrice == null) throw new InvalidParamException("OrderItemLine.OrderItemOption.itemOptionPrice");

        this.ordering = ordering;
        this.itemOptionName = itemOptionName;
        this.itemOptionPrice = itemOptionPrice;

        changeOrderItemOptionGroup(orderItemOptionGroup);
    }
}