package com.example.shop.repositiory.item.querydsl;

import com.example.shop.domain.item.Item;
import com.example.shop.repositiory.item.ItemSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.shop.domain.item.brands.QBrand.brand;
import static com.example.shop.domain.item.category.QCategory.category;
import static com.example.shop.domain.item.category.QCategoryItem.categoryItem;
import static com.example.shop.domain.item.Item.*;
import static com.example.shop.domain.item.QItem.*;
import static org.springframework.util.StringUtils.hasText;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Item> findAll(ItemSearchCondition condition, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        return queryFactory
                .select(item).distinct()
                .from(item)
                .leftJoin(item.brand, brand)
                .leftJoin(item.categoryItemList, categoryItem)
                .leftJoin(categoryItem.category, category)
                .where(searchValueEp(condition))
                .where(salesStatusEp(condition.getItemSalesStatus()))
                .where(displayStatusEp(condition.getItemDisplayStatus()))
                .where(
                        categoryNameContaining(condition.getPrimaryCategoryName(),
                                condition.getSecondaryCategoryName())
                )
                .where(searchStartDateTime(condition.getStartDatetime()))
                .where(searchEndDateTime(condition.getEndDatetime()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetch();
    }

    @Override
    public Integer itemTotalCount(ItemSearchCondition condition) {
       return queryFactory
                .select(item).distinct()
                .from(item)
                .where(searchValueEp(condition))
                .where(salesStatusEp(condition.getItemSalesStatus()))
                .where(displayStatusEp(condition.getItemDisplayStatus()))
                .where(
                        categoryNameContaining(condition.getPrimaryCategoryName(),
                                condition.getSecondaryCategoryName())
                )
                .where(searchStartDateTime(condition.getStartDatetime()))
                .where(searchEndDateTime(condition.getEndDatetime()))
                .fetch().size();
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "itemName":
                        ORDERS.add(new OrderSpecifier(direction, item.itemName));
                        break;

                    case "brand.brandName":
                        ORDERS.add(new OrderSpecifier(direction, brand.brandName));
                        break;

                    case "id":
                        ORDERS.add(new OrderSpecifier(direction, item.id));
                        break;

                    case "createdDate":
                        ORDERS.add(new OrderSpecifier(direction, item.createdAt));
                        break;

                    default:
                        break;
                }
            }
        }
        return ORDERS;
    }

    private BooleanExpression searchValueEp(ItemSearchCondition condition) {
        if (condition.getSearchType() == null) {
            return null;
        }

        BooleanExpression booleanExpression;
        switch (condition.getSearchType()) {
            case ITEM_NAME:
                booleanExpression = itemNameContaining(condition.getSearchValue());
                break;
            case ITEM_ID:
                booleanExpression = itemIdContaining(condition.getSearchValue());
                break;
            case ITEM_CODE:
                booleanExpression = itemCodeContaining(condition.getSearchValue());
                break;
            case BRAND_NAME:
                booleanExpression = brandNameContaining(condition.getSearchValue());
                break;
        default: booleanExpression = null;
        }

        return booleanExpression;
    }

    private BooleanExpression itemCodeContaining(String searchValue) {
        return hasText(searchValue) ? item.itemCode.contains(searchValue) : null;
    }

    private BooleanExpression itemNameContaining(String searchValue) {
        return hasText(searchValue) ? item.itemName.contains(searchValue) : null;
    }

    private BooleanExpression itemIdContaining(String searchValue) {
        return hasText(searchValue) ? item.id.stringValue().contains(searchValue) : null;
    }

    private BooleanExpression brandNameContaining(String searchValue) {
        return hasText(searchValue) ? item.brand.brandName.eq(searchValue) : null;
    }

    private BooleanBuilder categoryNameContaining(String primaryCategoryName, String secondaryCategoryName) {
        BooleanBuilder builder = new BooleanBuilder();

        if (primaryCategoryNameContaining(primaryCategoryName) != null) {
            builder.and(primaryCategoryNameContaining(primaryCategoryName));
        }

        if (secondaryCategoryNameContaining(secondaryCategoryName) != null) {
            builder.or(secondaryCategoryNameContaining(secondaryCategoryName));
        }
        return builder;
    }

    private BooleanExpression primaryCategoryNameContaining(String primaryCategoryName) {
        return hasText(primaryCategoryName) ? (category.categoryName.contains(primaryCategoryName))
                : null;
    }

    private BooleanExpression secondaryCategoryNameContaining(String secondaryCategoryName) {
        return hasText(secondaryCategoryName) ? category.categoryName.contains(secondaryCategoryName) : null;
    }

    private BooleanExpression salesStatusEp(SalesStatus salesStatus) {
        return  salesStatus != null ? item.itemSalesStatus.eq(salesStatus) : null;
    }

    private BooleanExpression displayStatusEp(DisplayStatus displayStatus) {
        return  displayStatus != null ? item.itemDisplayStatus.eq(displayStatus) : null;
    }

    private BooleanExpression searchStartDateTime(LocalDate startDateTime) {
        if(startDateTime == null)
            return null;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.of(startDateTime, LocalTime.MIN);
        return item.createdAt.goe(LocalDateTime.from(localDateTime));
    }

    private BooleanExpression searchEndDateTime(LocalDate endDateTime) {
        if(endDateTime == null)
            return null;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.of(endDateTime, LocalTime.MAX);
        return item.createdAt.loe(LocalDateTime.from(localDateTime));
    }
}
