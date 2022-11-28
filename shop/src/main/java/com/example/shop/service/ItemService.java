package com.example.shop.service;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.item.brands.Brand;
import com.example.shop.domain.item.category.Category;
import com.example.shop.domain.item.category.CategoryItem;
import com.example.shop.domain.item.Item;
import com.example.shop.domain.item.ItemImage;
import com.example.shop.domain.item.ItemOption;
import com.example.shop.domain.item.ItemOptionGroup;
import com.example.shop.dto.item.ItemDto;
import com.example.shop.repositiory.item.brand.BrandRepository;
import com.example.shop.repositiory.item.category.CategoryItemRepository;
import com.example.shop.repositiory.item.category.CategoryRepository;
import com.example.shop.repositiory.item.*;
import com.example.shop.repositiory.order.OrderRepository;
import com.example.shop.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.shop.dto.item.ItemDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final CategoryService categoryService;
    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionGroupRepository itemOptionGroupRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final ItemImageListRepository itemImageListRepository;
    private final OrderRepository orderRepository;
    private final FileStore fileStore;

    @Transactional
    public String saveItem(ItemDto.ItemForm form) {
        try {
            Brand brand = brandRepository.findByBrandToken(form.getBrandToken())
                    .orElseThrow(EntityNotFoundException::new);

            String itemCode = createItemCode(brand.getId());
            String mainImageName = getImageFile(form.getMainImageFile());
            Item item = form.toEntity(itemCode, mainImageName, brand);

            itemRepository.save(item);

            itemSeriesSave(form.getOptionGroups(), item);
            itemImagesSave(form.getImageFiles(), item);
            categoryItemSave(form.getPrimaryCategoryToken(), form.getSecondaryCategoryToken(), item);
            return item.getItemToken();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InvalidParamException("upload file not found");
        }
    }

    public ItemEditForm getItem(String itemToken) {
        Item item = itemRepository.findByItemToken(itemToken)
                .orElseThrow(EntityNotFoundException::new);

        return ItemEditForm.builder()
                .item(item)
                .imageFilePaths(getImageFilePaths(item))
                .primaryCategoryToken(getCategoryToken(item, Category.Level.PRIMARY_CATEGORY))
                .secondaryCategoryToken(getCategoryToken(item, Category.Level.SECONDARY_CATEGORY))
                .optionGroups(getOptionGroups(item))
                .build();
    }

    public ItemResponse getItemByItemToken(String itemToken) {
        Item item = itemRepository.findByItemToken(itemToken)
                .orElseThrow(EntityNotFoundException::new);

        String categoryHierarchy = categoryService.getCategoryHierarchy(item.getItemToken());
        List<String> itemImages = item.getItemImageList().stream()
                .map(ItemImage::getItemImageName)
                .collect(Collectors.toList());

        return new ItemResponse(item, categoryHierarchy, itemImages, getItemOptionSeries(item));
    }

    public Page<ItemResponse> searchItems(ItemSearchCondition condition, Pageable pageable) {
        List<Item> result = itemRepository.findAll(condition, pageable);
        List<ItemResponse> itemResponseList = result.stream().map(item -> {
            String categoryHierarchy = categoryService.getCategoryHierarchy(item.getItemToken());
            List<String> itemImages = item.getItemImageList().stream()
                    .map(ItemImage::getItemImageName)
                    .collect(Collectors.toList());

            return new ItemResponse(item, categoryHierarchy, itemImages, getItemOptionSeries(item));
        }).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(itemResponseList, pageable, () -> itemRepository.itemTotalCount(condition));
    }

    public Page<ItemResponse> searchItemByCategoryToken(String categoryToken, Pageable pageable) {
        return itemRepository.findAllByCategoryToken(categoryToken, pageable).map(item -> {
            String categoryHierarchy = categoryService.getCategoryHierarchy(item.getItemToken());
            List<String> itemImages = item.getItemImageList().stream()
                    .map(ItemImage::getItemImageName)
                    .collect(Collectors.toList());
            return new ItemResponse(item, categoryHierarchy, itemImages, getItemOptionSeries(item));
        });
    }

    public Page<ItemResponse> searchItemByBrandToken(String brandToken, Pageable pageable) {
        return itemRepository.findAllByBrand_BrandToken(brandToken, pageable).map(item -> {
            String categoryHierarchy = categoryService.getCategoryHierarchy(item.getItemToken());
            List<String> itemImages = item.getItemImageList().stream()
                    .map(ItemImage::getItemImageName)
                    .collect(Collectors.toList());
            return new ItemResponse(item, categoryHierarchy, itemImages, getItemOptionSeries(item));
        });
    }

    @Transactional
    public void updateItem(String itemToken, ItemEditForm form) {
        try {
            Item item = itemRepository.findByItemToken(itemToken)
                    .orElseThrow(EntityNotFoundException::new);

            Brand brand = brandRepository.findByBrandToken(form.getBrandToken())
                    .orElseThrow(EntityNotFoundException::new);

            String mainImageName = getImageFile(form.getMainImageFile());

            item.changeItem(form.getItemName(), form.getItemPrice(), brand, mainImageName, form.getItemContent(),
                    form.getItemSalesStatus(), form.getItemDisplayStatus());
            changeCategory(itemToken, form);

            itemOptionRepository.deleteAllByItemOptionGroup_Item_itemToken(itemToken);
            itemOptionGroupRepository.deleteAllByItem_ItemToken(itemToken);
            itemImageListRepository.deleteAllByItem_ItemToken(itemToken);

            itemImagesSave(form.getImageFiles(), item);
            itemSeriesSave(form.getOptionGroups(), item);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InvalidParamException("upload file not found");
        }
    }

    private String createItemCode(Long id) {
        Long maxId = itemRepository.findTopBrandIdByBrand_IdOrderByIdDesc(id).orElse(0L);
        return String.format("%04d%04d", id, maxId+1);
    }

    private String getImageFile(MultipartFile mainImageFile) throws IOException {
        return fileStore.storeFile(mainImageFile);
    }

    private void itemSeriesSave(Map<String, Set<String>> optionGroups, Item item) {
        Set<String> keys = optionGroups.keySet();
        int groupOrder = 0;

        for (String key : keys) {
            ItemOptionGroup itemOptionGroup = new ItemOptionGroup(item, groupOrder++, key.toString());
            ItemOptionGroup saveOptionGroup = itemOptionGroupRepository.save(itemOptionGroup);

            int optionOrder = 0;
            for (String value : optionGroups.get(key)) {
                ItemOption itemOption = new ItemOption(saveOptionGroup, optionOrder, value, 0);
                itemOptionRepository.save(itemOption);
            }
        }
    }

    private void itemImagesSave(List<MultipartFile> multipartFiles, Item item) throws IOException {
        List<String> uploadFiles = fileStore.storeFiles(multipartFiles);
        List<ItemImage> imageFiles = uploadFiles.stream()
                .map(uploadFile -> {
                    return new ItemImage(item, uploadFile);
                }).collect(Collectors.toList());

        itemImageListRepository.saveAll(imageFiles);
    }

    private void categoryItemSave(
            String primaryCategoryToken,
            String secondaryCategoryToken,
            Item item
    ) {
        Category primaryCategory = categoryRepository.findByCategoryToken(primaryCategoryToken)
                .orElseThrow(EntityNotFoundException::new);

        Category secondaryCategory = categoryRepository.findByCategoryToken(secondaryCategoryToken)
                .orElseThrow(EntityNotFoundException::new);

        CategoryItem primaryCategoryItem = new CategoryItem(item, primaryCategory);
        CategoryItem secondaryCategoryItem = new CategoryItem(item, secondaryCategory);
        categoryItemRepository.saveAll(Arrays.asList(primaryCategoryItem, secondaryCategoryItem));
    }

    private List<String> getImageFilePaths(Item item) {
        List<String> imageFilePaths = item.getItemImageList().stream()
                .map(ItemImage::getItemImageName)
                .collect(Collectors.toList());
        return imageFilePaths;
    }

    private String getCategoryToken(Item item, Category.Level primaryCategory) {
        return item.getCategoryItemList().stream()
                .filter(categoryItem -> categoryItem.getCategory().getLevel() == primaryCategory)
                .map(CategoryItem -> CategoryItem.getCategory().getCategoryToken())
                .findAny()
                .orElse("");
    }

    private Map<String, Set<String>> getOptionGroups(Item item) {
        Map<String, Set<String>> optionGroups = item.getItemOptionGroupList().stream()
                .collect(Collectors.toMap(
                                ItemOptionGroup::getItemOptionGroupName,
                                itemOptionGroup -> itemOptionGroup.getItemOptionList().stream()
                                        .map(ItemOption::getItemOptionName)
                                        .collect(Collectors.toSet())
                        )
                );
        return optionGroups;
    }

    private List<ItemOptionGroupResponse> getItemOptionSeries(Item item) {
        return item.getItemOptionGroupList().stream()
                .map(itemOptionGroup -> {
                    List<ItemOptionResponse> itemOptionResponseList = itemOptionGroup.getItemOptionList().stream()
                            .map(ItemOptionResponse::fromEntity)
                            .collect(Collectors.toList());

                    return ItemOptionGroupResponse.fromEntity(itemOptionGroup, itemOptionResponseList);
                }).collect(Collectors.toList());
    }

    private void changeCategory(String itemToken, ItemEditForm form) {
        Category primaryCategory = categoryRepository.findByCategoryToken(form.getPrimaryCategoryToken())
                .orElseThrow(EntityNotFoundException::new);

        Category secondaryCategory = categoryRepository.findByCategoryToken(form.getSecondaryCategoryToken())
                .orElseThrow(EntityNotFoundException::new);

        CategoryItem primaryCategoryItem = categoryItemRepository.findByItemTokenAndCategory_Level(itemToken, Category.Level.PRIMARY_CATEGORY)
                .stream().findAny()
                .orElseThrow(EntityNotFoundException::new);

        CategoryItem secondaryCategoryItem = categoryItemRepository.findByItemTokenAndCategory_Level(itemToken, Category.Level.SECONDARY_CATEGORY)
                .stream().findAny()
                .orElseThrow(EntityNotFoundException::new);

        primaryCategoryItem.setCategory(primaryCategory);
        secondaryCategoryItem.setCategory(secondaryCategory);
    }

}
