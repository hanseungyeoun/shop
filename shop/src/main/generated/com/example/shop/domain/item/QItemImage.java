package com.example.shop.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemImage is a Querydsl query type for ItemImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemImage extends EntityPathBase<ItemImage> {

    private static final long serialVersionUID = -961574704L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemImage itemImage = new QItemImage("itemImage");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final StringPath itemImageName = createString("itemImageName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QItemImage(String variable) {
        this(ItemImage.class, forVariable(variable), INITS);
    }

    public QItemImage(Path<? extends ItemImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemImage(PathMetadata metadata, PathInits inits) {
        this(ItemImage.class, metadata, inits);
    }

    public QItemImage(Class<? extends ItemImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

