package com.example.shop.domain.order.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItemOption is a Querydsl query type for OrderItemOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItemOption extends EntityPathBase<OrderItemOption> {

    private static final long serialVersionUID = 916554878L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItemOption orderItemOption = new QOrderItemOption("orderItemOption");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemOptionName = createString("itemOptionName");

    public final NumberPath<Long> itemOptionPrice = createNumber("itemOptionPrice", Long.class);

    public final NumberPath<Integer> ordering = createNumber("ordering", Integer.class);

    public final QOrderItemOptionGroup orderItemOptionGroup;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QOrderItemOption(String variable) {
        this(OrderItemOption.class, forVariable(variable), INITS);
    }

    public QOrderItemOption(Path<? extends OrderItemOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItemOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItemOption(PathMetadata metadata, PathInits inits) {
        this(OrderItemOption.class, metadata, inits);
    }

    public QOrderItemOption(Class<? extends OrderItemOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderItemOptionGroup = inits.isInitialized("orderItemOptionGroup") ? new QOrderItemOptionGroup(forProperty("orderItemOptionGroup"), inits.get("orderItemOptionGroup")) : null;
    }

}

