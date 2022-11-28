package com.example.shop.domain.order.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItem is a Querydsl query type for OrderItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItem extends EntityPathBase<OrderItem> {

    private static final long serialVersionUID = 1821536937L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItem orderItem = new QOrderItem("orderItem");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    public final NumberPath<Long> brandId = createNumber("brandId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final EnumPath<OrderItem.DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", OrderItem.DeliveryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final StringPath itemName = createString("itemName");

    public final NumberPath<Long> itemPrice = createNumber("itemPrice", Long.class);

    public final StringPath itemToken = createString("itemToken");

    public final com.example.shop.domain.order.QOrder order;

    public final NumberPath<Integer> orderCount = createNumber("orderCount", Integer.class);

    public final ListPath<OrderItemOptionGroup, QOrderItemOptionGroup> orderItemOptionGroupList = this.<OrderItemOptionGroup, QOrderItemOptionGroup>createList("orderItemOptionGroupList", OrderItemOptionGroup.class, QOrderItemOptionGroup.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QOrderItem(String variable) {
        this(OrderItem.class, forVariable(variable), INITS);
    }

    public QOrderItem(Path<? extends OrderItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItem(PathMetadata metadata, PathInits inits) {
        this(OrderItem.class, metadata, inits);
    }

    public QOrderItem(Class<? extends OrderItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new com.example.shop.domain.order.QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

