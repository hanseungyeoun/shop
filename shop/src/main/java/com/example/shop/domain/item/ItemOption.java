package com.example.shop.domain.item;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_option")
@ToString
public class ItemOption extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_group_id")
    @ToString.Exclude
    private ItemOptionGroup itemOptionGroup;
    private Integer ordering;
    private String itemOptionName;
    private Integer itemOptionPrice;

    public void changeItemOptionGroup(ItemOptionGroup itemOptionGroup) {
        if(this.itemOptionGroup != null) {
            this.itemOptionGroup.getItemOptionList().remove(this);
        }

        this.itemOptionGroup = itemOptionGroup;
        itemOptionGroup.getItemOptionList().add(this);
    }

    @Builder
    public ItemOption(
            ItemOptionGroup itemOptionGroup,
            Integer ordering,
            String itemOptionName,
            Integer itemOptionPrice
    ) {
        if (itemOptionGroup == null) throw new InvalidParamException("ItemOption.itemOptionGroup");
        if (ordering == null) throw new InvalidParamException("ItemOption.ordering");
        if (!StringUtils.hasText(itemOptionName)) throw new InvalidParamException("ItemOption.itemOptionName");
        if (itemOptionPrice == null) throw new InvalidParamException("ItemOption.itemOptionPrice");

        this.ordering = ordering;
        this.itemOptionName = itemOptionName;
        this.itemOptionPrice = itemOptionPrice;

        changeItemOptionGroup(itemOptionGroup);
    }
}
