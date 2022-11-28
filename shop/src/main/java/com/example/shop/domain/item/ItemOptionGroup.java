package com.example.shop.domain.item;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_option_group")
public class ItemOptionGroup extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Item item;
    private Integer ordering;
    private String itemOptionGroupName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemOptionGroup", cascade = CascadeType.PERSIST)
    private List<ItemOption> itemOptionList = new ArrayList<>();

    public void changeItem(Item item) {
        if(this.item != null) {
            this.item.getItemOptionGroupList().remove(this);
        }

        this.item = item;
        item.getItemOptionGroupList().add(this);
    }

    @Builder
    public ItemOptionGroup(Item item, Integer ordering, String itemOptionGroupName) {

        if (item == null) throw new InvalidParamException("ItemOptionGroup.item");
        if (ordering == null) throw new InvalidParamException("ItemOptionGroup.ordering");
        if (!StringUtils.hasText(itemOptionGroupName)) {
            throw new InvalidParamException("ItemOptionGroup.itemOptionGroupName");
        }


        this.ordering = ordering;
        this.itemOptionGroupName = itemOptionGroupName;
        changeItem(item);
    }
}
