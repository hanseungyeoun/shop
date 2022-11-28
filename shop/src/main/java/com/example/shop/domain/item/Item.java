package com.example.shop.domain.item;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.domain.item.brands.Brand;
import com.example.shop.domain.item.category.CategoryItem;
import com.example.shop.util.TokenGenerator;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
public class Item extends AbstractEntity {
    private static final String ITEM_PREFIX = "itm_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String itemToken;

    @Setter
    @Column(unique = true)
    private String itemCode;

    @Setter
    private String itemName;

    @Setter
    private Integer itemPrice;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Setter
    private String mainImageName;

    @Setter
    @Lob
    private String itemContent;

    @OneToMany(fetch = LAZY, mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemOptionGroup> itemOptionGroupList = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemImage> itemImageList = new ArrayList<>();

    @OneToMany( fetch = LAZY, mappedBy = "item")
    private List<CategoryItem> categoryItemList = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    private SalesStatus itemSalesStatus;

    @Setter
    @Enumerated(EnumType.STRING)
    private DisplayStatus itemDisplayStatus;

    @Getter
    @RequiredArgsConstructor
    public enum SalesStatus {
        ON_PREPARE("판매준비중"),
        ON_SALE("판매중"),
        OUT_OF_STOCK("품절");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DisplayStatus {
        ON_NOT_DISPLAY("미진열"),
        ON_DISPLAY("진열");
        private final String description;
    }

    //생성 로직
    @Builder
    public Item (
        String itemCode,
        String itemName,
        Integer itemPrice,
        Brand brand,
        String mainImageName,
        String itemContent
    ) {
        if(!StringUtils.hasText(itemCode)) throw new InvalidParamException("Item.itemCode");
        if(!StringUtils.hasText(itemName)) throw new InvalidParamException("Item.itemName");
        if(itemPrice == null) throw new InvalidParamException("Item.itemPrice");
        if(brand == null) throw new InvalidParamException("Item.brand");
        if(!StringUtils.hasText(mainImageName)) throw new InvalidParamException("Item.mainImage");
        if(!StringUtils.hasText(itemContent)) throw new InvalidParamException("Item.itemContent");

        this.itemToken = TokenGenerator.randomCharacterWithPrefix(ITEM_PREFIX);
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.brand = brand;
        this.mainImageName = mainImageName;
        this.itemContent = itemContent;
        this.itemSalesStatus = SalesStatus.ON_PREPARE;
        this.itemDisplayStatus = DisplayStatus.ON_NOT_DISPLAY;
    }

    public void changeItem(
           String itemName,
           Integer itemPrice,
           Brand brand,
           String mainImageName,
           String itemContent,
           Item.SalesStatus itemSalesStatus,
           Item.DisplayStatus itemDisplayStatus
    ) {
        if (StringUtils.hasText(itemName)) {
            this.setItemName(itemName);
        }

        if (itemPrice != null) {
            this.itemPrice = itemPrice;
        }

        if (brand != null) {
            this.setBrand(brand);
        }

        if (StringUtils.hasText(mainImageName)) {
            this.mainImageName = mainImageName;
        }

        if (StringUtils.hasText(itemContent)) {
            this.itemContent = itemContent;
        }

        if (StringUtils.hasText(itemContent)) {
            this.itemContent = itemContent;
        }


        if (itemSalesStatus != null) {
            this.itemSalesStatus = itemSalesStatus;
        }

        if (itemDisplayStatus != null) {
            this.itemDisplayStatus = itemDisplayStatus;
        }
    }

    //비지니스 로직
    public void changeOnSale() {
        this.itemSalesStatus = SalesStatus.ON_SALE;
        this.itemDisplayStatus = DisplayStatus.ON_DISPLAY;
    }

    public void changeOutOfStock() {
        this.itemSalesStatus = SalesStatus.OUT_OF_STOCK;
        this.itemDisplayStatus = DisplayStatus.ON_NOT_DISPLAY;
    }
}
