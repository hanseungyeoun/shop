package com.example.shop.service;

import com.example.shop.common.exception.*;
import com.example.shop.domain.item.category.Category;
import com.example.shop.dto.item.category.CategorySearchCondition;
import com.example.shop.repositiory.item.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.shop.dto.item.category.CategoryDto.CategoryForm;
import static com.example.shop.dto.item.category.CategoryDto.CategoryResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveCategory(CategoryForm form) {
        if (form.getLevel() == Category.Level.PRIMARY_CATEGORY) {
            Category savedCategory = categoryRepository.save(form.toEntity());
            return savedCategory.getCategoryToken();
        } else {
            Category category = categoryRepository.findByCategoryToken(form.getPrimaryCategoryToken())
                    .orElseThrow(EntityNotFoundException::new);
            categoryRepository.save(form.toEntity(category));
            return category.getCategoryToken();
        }
    }

    public List<CategoryResponse> getPrimaryCategories() {
        return categoryRepository.findAllPrimaryCategory().stream()
                .map(category -> {
                    List<CategoryResponse> categoryChildDtoList = category.getChildren().stream()
                            .map(CategoryResponse::fromEntity)
                            .collect(Collectors.toList());

                    return CategoryResponse.of(category, categoryChildDtoList);
                }).collect(Collectors.toList());
    }

    public CategoryForm getCategory(String token) {
        return categoryRepository.findByCategoryToken(token)
                .map(CategoryForm::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 없습니다 - token: " + token));
    }

    public String getCategoryHierarchy(String itemToken) {
        return categoryRepository.findByCategoryItemList_Item_itemToken(itemToken).stream()
                .sorted(Comparator.comparing(a -> a.getLevel().getDepth()))
                .map(Category::getCategoryName)
                .collect(Collectors.joining(" < "));
    }

    @Transactional
    public void updateCategory(String token, CategoryForm form) {
        Category category = categoryRepository.findByCategoryToken(token)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 없습니다 - token: " + token));

        category.changeInfo(form.getCategoryName(), form.getDescription());
    }
    
    @Transactional
    public void deleteCategory(String token) {
        categoryRepository.deleteByCategoryToken(token);
    }

    public List<CategoryResponse> getChildCategory(CategorySearchCondition condition) {
        List<CategoryResponse> childDtoList = null;

        switch (condition.getSearchType()) {
            case CATEGORY_TOKEN:
                return childDtoList = categoryRepository.findByParent_CategoryToken(condition.getSearchValue()).stream()
                        .map(category -> CategoryResponse.fromEntity(category))
                        .collect(Collectors.toList());
            case CATEGORY_NAME:
                return childDtoList = categoryRepository.findByParent_CategoryName(condition.getSearchValue()).stream()
                        .map(category -> CategoryResponse.fromEntity(category))
                        .collect(Collectors.toList());
            default:
                throw new InvalidParamException("CategorySearchCondition.CategorySearchType");
        }
    }
}
