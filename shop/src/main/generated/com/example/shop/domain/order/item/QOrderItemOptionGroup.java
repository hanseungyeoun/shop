package com.example.shop.domain.order.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItemOptionGroup is a Querydsl query type for OrderItemOptionGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItemOptionGroup extends EntityPathBase<OrderItemOptionGroup> {

    private static final long serialVersionUID = -523132767L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItemOptionGroup orderItemOptionGroup = new QOrderItemOptionGroup("orderItemOptionGroup");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemOptionGroupName = createString("itemOptionGroupName");

    public final NumberPath<Integer> ordering = createNumber("ordering", Integer.class);

    public final QOrderItem orderItem;

    public final ListPath<OrderItemOption, QOrderItemOption> orderItemOptionList = this.<OrderItemOption, QOrderItemOption>createList("orderItemOptionList", OrderItemOption.class, QOrderItemOption.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QOrderItemOptionGroup(String variable) {
        this(OrderItemOptionGroup.class, forVariable(variable), INITS);
    }

    public QOrderItemOptionGroup(Path<? extends OrderItemOptionGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItemOptionGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItemOptionGroup(PathMetadata metadata, PathInits inits) {
        this(OrderItemOptionGroup.class, metadata, inits);
    }

    public QOrderItemOptionGroup(Class<? extends OrderItemOptionGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderItem = inits.isInitialized("orderItem") ? new QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
    }

}

