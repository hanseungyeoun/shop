package com.example.shop.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1743246485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final com.example.shop.domain.order.fragment.QDeliveryFragment deliveryFragment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> orderedAt = createDateTime("orderedAt", java.time.LocalDateTime.class);

    public final ListPath<com.example.shop.domain.order.item.OrderItem, com.example.shop.domain.order.item.QOrderItem> orderItemList = this.<com.example.shop.domain.order.item.OrderItem, com.example.shop.domain.order.item.QOrderItem>createList("orderItemList", com.example.shop.domain.order.item.OrderItem.class, com.example.shop.domain.order.item.QOrderItem.class, PathInits.DIRECT2);

    public final StringPath orderToken = createString("orderToken");

    public final StringPath payMethod = createString("payMethod");

    public final EnumPath<Order.Status> status = createEnum("status", Order.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.deliveryFragment = inits.isInitialized("deliveryFragment") ? new com.example.shop.domain.order.fragment.QDeliveryFragment(forProperty("deliveryFragment")) : null;
    }

}

