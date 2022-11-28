package com.example.shop.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderStatistics is a Querydsl query type for OrderStatistics
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderStatistics extends EntityPathBase<OrderStatistics> {

    private static final long serialVersionUID = 1893969646L;

    public static final QOrderStatistics orderStatistics = new QOrderStatistics("orderStatistics");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QOrderStatistics(String variable) {
        super(OrderStatistics.class, forVariable(variable));
    }

    public QOrderStatistics(Path<? extends OrderStatistics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderStatistics(PathMetadata metadata) {
        super(OrderStatistics.class, metadata);
    }

}

