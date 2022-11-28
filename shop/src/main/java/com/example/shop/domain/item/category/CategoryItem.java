package com.example.shop.domain.item.category;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.domain.item.Item;
import lombok.*;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_item")
public class CategoryItem extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void changeItem(Item item) {
        if(this.item != null) {
            this.item.getCategoryItemList().remove(this);
        }

        this.item = item;
        item.getCategoryItemList().add(this);
    }

    public void changeCategory(Category category) {
        if(this.category != null) {
            this.category.getCategoryItemList().remove(this);
        }

        this.category = category;
        category.getCategoryItemList().add(this);
    }

    @Builder
    public CategoryItem(Item item, Category category) {
        if (item == null) throw new InvalidParamException("CategoryItem.item");
        if (category == null) throw new InvalidParamException("CategoryItem.category");

        changeCategory(category);
        changeItem(item);
    }
}