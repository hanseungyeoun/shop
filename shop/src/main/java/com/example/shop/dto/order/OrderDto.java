package com.example.shop.dto.order;

import com.example.shop.domain.item.Item;
import com.example.shop.domain.order.Order;
import com.example.shop.domain.order.fragment.DeliveryFragment;
import com.example.shop.domain.order.item.OrderItem;
import com.example.shop.domain.order.item.OrderItemOption;
import com.example.shop.domain.order.item.OrderItemOptionGroup;
import com.example.shop.service.payment.PayMethod;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Data
    public static class RegisterOrderRequest {
        @NotNull(message = "userId 는 필수값입니다")
        private Long userId;

        @NotBlank(message = "payMethod 는 필수값입니다")
        private String payMethod;

        @NotBlank(message = "receiverName 는 필수값입니다")
        private String receiverName;

        @NotBlank(message = "receiverPhone 는 필수값입니다")
        private String receiverPhone;

        @NotBlank(message = "receiverZipcode 는 필수값입니다")
        private String receiverZipcode;

        @NotBlank(message = "receiverAddress1 는 필수값입니다")
        private String receiverAddress1;

        @NotBlank(message = "receiverAddress2 는 필수값입니다")
        private String receiverAddress2;

        @NotBlank(message = "etcMessage 는 필수값입니다")
        private String etcMessage;

        private List<RegisterOrderItem> orderItemList;

        public Order toEntity(String username) {
            DeliveryFragment deliveryFragment = DeliveryFragment.builder()
                    .receiverName(receiverName)
                    .receiverPhone(receiverPhone)
                    .receiverZipcode(receiverZipcode)
                    .receiverAddress1(receiverAddress1)
                    .receiverAddress2(receiverAddress2)
                    .etcMessage(etcMessage)
                    .build();

            return Order.builder()
                    .userId(userId)
                    .userName(username)
                    .payMethod(payMethod)
                    .deliveryFragment(deliveryFragment)
                    .build();
        }
    }

    @Data
    public static class RegisterOrderItem {
        @NotNull(message = "brandId 는 필수값입니다")
        private Long brandId;

        @NotNull(message = "orderCount 는 필수값입니다")
        private Integer orderCount;

        @NotBlank(message = "itemToken 는 필수값입니다")
        private String itemToken;

        @NotBlank(message = "itemName 는 필수값입니다")
        private String itemName;

        @NotNull(message = "itemPrice 는 필수값입니다")
        private Long itemPrice;

        private List<RegisterOrderItemOptionGroupRequest> orderItemOptionGroupList;

        public OrderItem toEntity(Order order, Item item) {
            return OrderItem.builder()
                    .order(order)
                    .orderCount(orderCount)
                    .brandId(brandId)
                    .itemId(item.getId())
                    .itemToken(item.getItemToken())
                    .itemName(itemName)
                    .itemPrice(itemPrice)
                    .build();
        }
    }

    @Data
    public static class RegisterOrderItemOptionGroupRequest {
        @NotNull(message = "ordering 는 필수값입니다")
        private Integer ordering;

        @NotBlank(message = "itemOptionGroupName 는 필수값입니다")
        private String itemOptionGroupName;

        private List<RegisterOrderItemOptionRequest> orderItemOptionList;

        public OrderItemOptionGroup toEntity(OrderItem orderItem) {
            return OrderItemOptionGroup.builder()
                    .orderItem(orderItem)
                    .ordering(ordering)
                    .itemOptionGroupName(itemOptionGroupName)
                    .build();
        }
    }

    @Data
    public static class RegisterOrderItemOptionRequest {
        @NotNull(message = "ordering 는 필수값입니다")
        private Integer ordering;

        @NotBlank(message = "itemOptionName 는 필수값입니다")
        private String itemOptionName;

        @NotNull(message = "itemOptionPrice 는 필수값입니다")
        private Long itemOptionPrice;

        public OrderItemOption toEntity(OrderItemOptionGroup orderItemOptionGroup) {
            return OrderItemOption.builder()
                    .orderItemOptionGroup(orderItemOptionGroup)
                    .ordering(ordering)
                    .itemOptionName(itemOptionName)
                    .itemOptionPrice(itemOptionPrice)
                    .build();
        }
    }

    @Getter
    @Builder
    @ToString
    public static class RegisterOrderResponse {
        private final String orderToken;

        public RegisterOrderResponse(String orderToken) {
            this.orderToken = orderToken;
        }
    }

    @Data
    public static class OrderResponse {
        private final Long orderId;
        private final String orderToken;
        private final Long userId;
        private final String userName;
        private final String payMethod;
        private final Long totalAmount;
        private final DeliveryInfo deliveryInfo;
        private final LocalDateTime orderedAt;
        private final String status;
        private final String statusDescription;
        private final LocalDateTime createdAt;
        private final List<OrderItemResponse> orderItemList;

        public OrderResponse(Order order, List<OrderItemResponse> orderItemList) {
            this.orderId = order.getId();
            this.orderToken = order.getOrderToken();
            this.userId = order.getUserId();
            this.userName = order.getUserName();
            this.payMethod = order.getPayMethod();
            this.totalAmount = order.calculateTotalAmount();
            this.deliveryInfo = new DeliveryInfo(order.getDeliveryFragment());
            this.orderedAt = order.getOrderedAt();
            this.status = order.getStatus().name();
            this.statusDescription = order.getStatus().getDescription();
            this.createdAt = order.getCreatedAt();
            this.orderItemList = orderItemList;
        }
    }

    @Data
    @AllArgsConstructor
    public static class DeliveryInfo {
        private final String receiverPhone;
        private final String receiverZipcode;
        private final String receiverName;
        private final String receiverAddress1;
        private final String receiverAddress2;
        private final String etcMessage;

        public DeliveryInfo(DeliveryFragment deliveryFragment) {
            this.receiverPhone = deliveryFragment.getReceiverPhone();
            this.receiverZipcode = deliveryFragment.getReceiverName();
            this.receiverName = deliveryFragment.getReceiverAddress1();
            this.receiverAddress1 = deliveryFragment.getReceiverAddress1();
            this.receiverAddress2 = deliveryFragment.getReceiverAddress2();
            this.etcMessage = deliveryFragment.getEtcMessage();
        }
    }

    @Data
    public static class OrderItemResponse {
        private final Long orderItemId;
        private final Integer orderCount;
        private final Long brandId;
        private final Long itemId;
        private final String itemName;
        private final Long totalAmount;
        private final Long itemPrice;
        private final String deliveryStatus;
        private final String deliveryStatusDescription;
        private final String optionName;
        private final List<OrderItemOptionGroupResponse> orderItemOptionGroupList;

        public OrderItemResponse(OrderItem orderItem, List<OrderItemOptionGroupResponse> orderItemOptionGroupList) {
            this.orderItemId = orderItem.getItemId();
            this.orderCount = orderItem.getOrderCount();
            this.brandId = orderItem.getBrandId();
            this.itemId = orderItem.getItemId();
            this.itemName = orderItem.getItemName();
            this.totalAmount = orderItem.calculateTotalAmount();
            this.itemPrice = orderItem.getItemPrice();
            this.deliveryStatus = orderItem.getDeliveryStatus().name();
            this.deliveryStatusDescription = orderItem.getDeliveryStatus().getDescription();
            this.orderItemOptionGroupList = orderItemOptionGroupList;
            this.optionName = orderItem.getOptionName();
        }
    }

    @Data
    public static class OrderItemOptionGroupResponse {
        private final Integer ordering;
        private final String itemOptionGroupName;
        private final List<OrderItemOptionResponse> orderItemOptionList;

        public OrderItemOptionGroupResponse(OrderItemOptionGroup orderItemOptionGroup, List<OrderItemOptionResponse> orderItemOptionList) {
            this.ordering = orderItemOptionGroup.getOrdering();
            this.itemOptionGroupName = orderItemOptionGroup.getItemOptionGroupName();
            this.orderItemOptionList = orderItemOptionList;
        }
    }

    @Data
    public static class OrderItemOptionResponse {
        private final Integer ordering;
        private final String itemOptionName;
        private final Long itemOptionPrice;

        public OrderItemOptionResponse(OrderItemOption orderItemOption) {
            this.ordering = orderItemOption.getOrdering();
            this.itemOptionName = orderItemOption.getItemOptionName();
            this.itemOptionPrice = orderItemOption.getItemOptionPrice();
        }
    }

    @Data
    @NoArgsConstructor
    public static class PaymentRequest {
        @NotBlank(message = "orderToken 는 필수값입니다")
        private String orderToken;

        @NotNull(message = "payMethod 는 필수값입니다")
        private PayMethod payMethod;

        @NotNull(message = "amount 는 필수값입니다")
        private Long amount;

        @NotBlank(message = "orderDescription 는 필수값입니다")
        private String orderDescription;
    }
}
