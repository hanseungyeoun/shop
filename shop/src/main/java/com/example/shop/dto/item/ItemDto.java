package com.example.shop.dto.item;

import com.example.shop.domain.item.brands.Brand;
import com.example.shop.domain.item.Item;
import com.example.shop.domain.item.ItemOption;
import com.example.shop.domain.item.ItemOptionGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemDto
{
    @Data
    public static class ItemForm {

        @NotBlank(message = "브랜드를 선택하세요!")
        private String brandToken;

        @NotBlank(message = "상품명을 입력하세요!")
        private String itemName;

        @NotNull(message = "가격을 입력하세요!")
        private Integer itemPrice;

        @NotBlank(message = "상품상세정보를 입력하세요!")
        private String itemContent;

        @NotNull(message = "메인이미지를 선택하세요!")
        private MultipartFile mainImageFile;

        @NotEmpty(message = "상품 이미지를 선택하세요!")
        private List<MultipartFile> imageFiles;

        @NotBlank(message = "1차 카테고리를 선택하세요!")
        private String primaryCategoryToken;

        @NotBlank(message = "2차 카테고리를 선택하세요!")
        private String secondaryCategoryToken;

        @NotEmpty(message = "2차 카테고리를 선택하세요!")
        private Map<String, Set<String>> optionGroups;

        private Item.SalesStatus itemSalesStatus;
        private Item.DisplayStatus itemDisplayStatus;

        public ItemForm() {
            this.setItemSalesStatus(Item.SalesStatus.ON_PREPARE);
            this.setItemDisplayStatus(Item.DisplayStatus.ON_NOT_DISPLAY);
        }

        public Item toEntity(String itemCode, String mainImageName, Brand brand) {
            return Item.builder()
                    .itemCode(itemCode)
                    .itemName(itemName)
                    .itemPrice(itemPrice)
                    .brand(brand)
                    .mainImageName(mainImageName)
                    .itemContent(itemContent)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemEditForm {

        @NotBlank(message = "브랜드를 선택하세요!")
        private String brandToken;

        @NotBlank(message = "상품명을 입력하세요!")
        private String itemName;

        @NotNull(message = "가격을 입력하세요!")
        private Integer itemPrice;

        @NotBlank(message = "상품상세정보를 입력하세요!")
        private String itemContent;

        @NotNull(message = "메인이미지를 선택하세요!")
        private MultipartFile mainImageFile;

        @NotEmpty(message = "상품 이미지를 선택하세요!")
        private List<MultipartFile> imageFiles;

        @NotBlank(message = "1차 카테고리를 선택하세요!")
        private String primaryCategoryToken;

        @NotBlank(message = "2차 카테고리를 선택하세요!")
        private String secondaryCategoryToken;

        private Map<String, Set<String>> optionGroups;
        private Item.SalesStatus itemSalesStatus;
        private Item.DisplayStatus itemDisplayStatus;

        private String mainImageFilePath;
        private List<String> imageFilePaths;

        @Builder
        private ItemEditForm(
                Item item,
                List<String> imageFilePaths,
                String primaryCategoryToken,
                String secondaryCategoryToken,
                Map<String, Set<String>> optionGroups
        ) {
            this.brandToken =  item.getBrand().getBrandToken();
            this.itemName =  item.getItemName();
            this.itemPrice =  item.getItemPrice();
            this.itemContent =  item.getItemContent();
            this.itemSalesStatus =  item.getItemSalesStatus();
            this.itemDisplayStatus =  item.getItemDisplayStatus();
            this.mainImageFilePath =  item.getMainImageName();
            this.imageFilePaths =  imageFilePaths;
            this.primaryCategoryToken =  primaryCategoryToken;
            this.secondaryCategoryToken =  secondaryCategoryToken;
            this.optionGroups =  optionGroups;
        }

        public Item toEntity(String itemCode, String mainImageName, Brand brand) {
            return Item.builder()
                    .itemCode(itemCode)
                    .itemName(itemName)
                    .itemPrice(itemPrice)
                    .brand(brand)
                    .mainImageName(mainImageName)
                    .itemContent(itemContent)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemResponse {
        private Long id;
        private String itemToken;
        private String itemCode;
        private String itemName;
        private Integer itemPrice;
        private String brandName;
        private String itemContent;
        private String mainImageName;
        private Item.SalesStatus itemSalesStatus;
        private Item.DisplayStatus itemDisplayStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String categoryHierarchy;
        private List<String> itemImageList;
        private List<ItemOptionGroupResponse> itemOptionGroupResponseList;

        @Builder
        public ItemResponse(
                Item item,
                String categoryHierarchy,
                List<String> itemImageList,
                List<ItemOptionGroupResponse> itemOptionGroupResponseList
        ) {
            ItemResponse itemResponse = new ItemResponse();
            this.id = item.getId();
            this.itemToken = item.getItemToken();
            this.itemCode = item.getItemCode();
            this.itemName = item.getItemName();
            this.itemPrice = item.getItemPrice();
            this.brandName = item.getBrand().getBrandName();
            this.itemContent = item.getItemContent();
            this.mainImageName = item.getMainImageName();
            this.itemSalesStatus = item.getItemSalesStatus();
            this.itemDisplayStatus = item.getItemDisplayStatus();
            this.createdAt = item.getCreatedAt();
            this.updatedAt = item.getUpdatedAt();
            this.categoryHierarchy = categoryHierarchy;
            this.itemImageList = itemImageList;
            this.itemOptionGroupResponseList = itemOptionGroupResponseList;
       }
    }

    @Data
    @NoArgsConstructor
    public static class ItemOptionGroupResponse {
        private Long id;
        private Integer ordering;
        private String itemOptionGroupName;
        private List<ItemOptionResponse> itemOptionResponseList;

        public static ItemOptionGroupResponse fromEntity(ItemOptionGroup itemOptionGroup, List<ItemOptionResponse> itemOptionResponseList) {
            ItemOptionGroupResponse itemOptionGroupResponse = new ItemOptionGroupResponse();
            itemOptionGroupResponse.id = itemOptionGroup.getId();
            itemOptionGroupResponse.ordering = itemOptionGroup.getOrdering();
            itemOptionGroupResponse.itemOptionGroupName = itemOptionGroup.getItemOptionGroupName();
            itemOptionGroupResponse.itemOptionResponseList = itemOptionResponseList;
            return itemOptionGroupResponse;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ItemOptionResponse {

        private Long id;
        private Integer ordering;
        private String itemOptionName;
        private Integer itemOptionPrice;

        public static ItemOptionResponse fromEntity(ItemOption itemOption) {
            ItemOptionResponse itemOptionResponse = new ItemOptionResponse();
            itemOptionResponse.id = itemOption.getId();
            itemOptionResponse.ordering = itemOption.getOrdering();
            itemOptionResponse.itemOptionName = itemOption.getItemOptionName();
            itemOptionResponse.itemOptionPrice = itemOption.getItemOptionPrice();
            return itemOptionResponse;
        }
    }
}
