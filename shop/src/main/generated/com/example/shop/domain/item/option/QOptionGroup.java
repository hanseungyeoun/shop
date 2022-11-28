package com.example.shop.domain.item.option;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionGroup is a Querydsl query type for OptionGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionGroup extends EntityPathBase<OptionGroup> {

    private static final long serialVersionUID = 2044357897L;

    public static final QOptionGroup optionGroup = new QOptionGroup("optionGroup");

    public final com.example.shop.domain.QAbstractEntity _super = new com.example.shop.domain.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath optionGroupCode = createString("optionGroupCode");

    public final StringPath optionGroupName = createString("optionGroupName");

    public final StringPath optionGroupToken = createString("optionGroupToken");

    public final ListPath<Option, QOption> optionList = this.<Option, QOption>createList("optionList", Option.class, QOption.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QOptionGroup(String variable) {
        super(OptionGroup.class, forVariable(variable));
    }

    public QOptionGroup(Path<? extends OptionGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOptionGroup(PathMetadata metadata) {
        super(OptionGroup.class, metadata);
    }

}

