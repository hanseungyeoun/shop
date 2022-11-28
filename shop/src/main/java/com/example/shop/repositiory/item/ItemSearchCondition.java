package com.example.shop.repositiory.item;

import com.example.shop.common.constant.ItemSearchType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

import static com.example.shop.domain.item.Item.*;


@Data
public class ItemSearchCondition implements Serializable {
    private ItemSearchType searchType;
    private String searchValue;
    private String primaryCategoryName;
    private String secondaryCategoryName;

    private SalesStatus itemSalesStatus;
    private DisplayStatus itemDisplayStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDatetime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDatetime;

    public ItemSearchCondition() {
        this.itemSalesStatus = SalesStatus.ON_PREPARE;
        this.itemDisplayStatus = DisplayStatus.ON_NOT_DISPLAY;
    }
}
