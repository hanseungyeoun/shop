package com.example.shop.domain.order.fragment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryFragment is a Querydsl query type for DeliveryFragment
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDeliveryFragment extends BeanPath<DeliveryFragment> {

    private static final long serialVersionUID = -133598689L;

    public static final QDeliveryFragment deliveryFragment = new QDeliveryFragment("deliveryFragment");

    public final StringPath etcMessage = createString("etcMessage");

    public final StringPath receiverAddress1 = createString("receiverAddress1");

    public final StringPath receiverAddress2 = createString("receiverAddress2");

    public final StringPath receiverName = createString("receiverName");

    public final StringPath receiverPhone = createString("receiverPhone");

    public final StringPath receiverZipcode = createString("receiverZipcode");

    public QDeliveryFragment(String variable) {
        super(DeliveryFragment.class, forVariable(variable));
    }

    public QDeliveryFragment(Path<? extends DeliveryFragment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryFragment(PathMetadata metadata) {
        super(DeliveryFragment.class, metadata);
    }

}

