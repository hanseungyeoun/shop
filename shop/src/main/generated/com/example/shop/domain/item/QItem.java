package com.example.shop.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1787820213L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    public final com.example.shop.domain.item.brands.QBrand brand;

    public final ListPath<com.example.shop.domain.item.category.CategoryItem, com.example.shop.domain.item.category.QCategoryItem> categoryItemList = this.<com.example.shop.domain.item.category.CategoryItem, com.example.shop.domain.item.category.QCategoryItem>createList("categoryItemList", com.example.shop.domain.item.category.CategoryItem.class, com.example.shop.domain.item.category.QCategoryItem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemCode = createString("itemCode");

    public final StringPath itemContent = createString("itemContent");

    public final EnumPath<Item.DisplayStatus> itemDisplayStatus = createEnum("itemDisplayStatus", Item.DisplayStatus.class);

    public final ListPath<ItemImage, QItemImage> itemImageList = this.<ItemImage, QItemImage>createList("itemImageList", ItemImage.class, QItemImage.class, PathInits.DIRECT2);

    public final StringPath itemName = createString("itemName");

    public final ListPath<ItemOptionGroup, QItemOptionGroup> itemOptionGroupList = this.<ItemOptionGroup, QItemOptionGroup>createList("itemOptionGroupList", ItemOptionGroup.class, QItemOptionGroup.class, PathInits.DIRECT2);

    public final NumberPath<Integer> itemPrice = createNumber("itemPrice", Integer.class);

    public final EnumPath<Item.SalesStatus> itemSalesStatus = createEnum("itemSalesStatus", Item.SalesStatus.class);

    public final StringPath itemToken = createString("itemToken");

    public final StringPath mainImageName = createString("mainImageName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new com.example.shop.domain.item.brands.QBrand(forProperty("brand")) : null;
    }

}

