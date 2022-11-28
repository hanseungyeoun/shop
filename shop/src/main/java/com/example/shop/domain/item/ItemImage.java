package com.example.shop.domain.item;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Item_image")
public class ItemImage extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    private String itemImageName;

    public void changeItemImage(Item item) {
        if(this.item != null) {
            this.item.getItemImageList().remove(this);
        }

        this.item = item;
        item.getItemImageList().add(this);
    }

    @Builder
    public ItemImage(Item item, String uploadImages) {
        if (uploadImages == null) throw new InvalidParamException("ItemImage.uploadImages");

        this.itemImageName = uploadImages;
        changeItemImage(item);
    }
}