package com.example.shop.fixture;

import com.example.shop.domain.item.category.Category;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.example.shop.dto.item.category.CategoryDto.*;

public class CategoryFixture {

    public static Category createCategory(String name, String desc) {
        Category category = Category.categoryBuilder()
                .categoryName(name)
                .description(desc)
                .build();

        ReflectionTestUtils.setField(category, "id", 1L);
        return category;
    }

    public static Category createCategory() {
        return createCategory("name", "desc");
    }

    public static Category createCategory(String name, String desc, Category parent) {
        Category category = Category.categoryWithParentBuilder()
                .categoryName(name)
                .description(desc)
                .parent(parent)
                .build();

        ReflectionTestUtils.setField(category, "id", 1L);
        return category;
    }

    public static Category createCategoryWithParent() {
        return createCategory("name", "desc", createCategory());
    }

    public static CategoryForm createCategoryFrom(String name, String desc) {
        CategoryForm form = new CategoryForm();
        form.setCategoryName(name);
        form.setDescription(desc);
        form.setLevel(Category.Level.PRIMARY_CATEGORY);

        return form;
    }

    public static CategoryForm createCategoryFrom() {
        return createCategoryFrom("name", "desc");
    }

    public static CategoryForm createCategoryFrom(String name, String desc, String parentToken) {
        CategoryForm form = new CategoryForm();
        form.setCategoryName(name);
        form.setDescription(desc);
        form.setPrimaryCategoryToken(parentToken);
        form.setLevel(Category.Level.SECONDARY_CATEGORY);

        return form;
    }

    public static CategoryForm createCategoryFromWithParent() {
        return createCategoryFrom("name", "desc", "token");
    }

    public static List<CategoryResponse> createResponses(){
        return List.of(createResponse(1L, "token", "name", "desc"));
    }

    public static CategoryResponse createResponse(Long id, String token, String name, String desc) {
        CategoryResponse response = new CategoryResponse();
        response.setId(id);
        response.setCategoryToken(token);
        response.setCategoryName(name);
        response.setDescription(desc);
        response.setLevel(Category.Level.PRIMARY_CATEGORY);

        return response;
    }

    public static CategoryResponse createResponse(Long id, Long parentId , String token, String name, String desc) {
        CategoryResponse response = new CategoryResponse();
        response.setId(id);
        response.setParentId(parentId);
        response.setCategoryToken(token);
        response.setCategoryName(name);
        response.setDescription(desc);
        response.setLevel(Category.Level.SECONDARY_CATEGORY);

        return response;
    }
}
