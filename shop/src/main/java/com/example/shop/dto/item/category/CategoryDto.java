package com.example.shop.dto.item.category;

import com.example.shop.domain.item.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.shop.domain.item.category.Category.*;

public class CategoryDto {

    @Data
    public static class CategoryForm {

        @NotBlank(message = "카테고리명은 필수값입니다")
        private String categoryName;

        @NotBlank(message = "카테고리명은 필수값입니다")
        private String description;

        private Category.Level level;

        private String primaryCategoryToken;

        public CategoryForm() {
            this.level = Category.Level.PRIMARY_CATEGORY;
        }

        public Category toEntity() {
            return new Category(categoryName, description);
        }

        public Category toEntity(Category parent) {
            return new Category(parent, categoryName, description);
        }

        public static CategoryForm fromEntity(Category category) {
            CategoryForm form = new CategoryForm();
            form.categoryName = category.getCategoryName();
            form.description = category.getDescription();
            form.level = category.getLevel();

            if(category.getLevel() == Category.Level.SECONDARY_CATEGORY) {
                form.primaryCategoryToken = category.getParent().getCategoryToken();
            }

            return form;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryResponse {
        private Long id;
        private String categoryToken;
        private Long parentId;
        private String categoryName;
        private String description;
        private Level level;
        private List<CategoryResponse> childList = new ArrayList<>();

        public static CategoryResponse fromEntity(Category category) {
            return new CategoryResponse(
                    category.getId(),
                    category.getCategoryToken(),
                    category.getParentId(),
                    category.getCategoryName(),
                    category.getDescription(),
                    category.getLevel(),
                    Collections.EMPTY_LIST
            );
        }

        public static CategoryResponse of(Category category, List<CategoryResponse> childList) {
            return new CategoryResponse(
                    category.getId(),
                    category.getCategoryToken(),
                    category.getParentId(),
                    category.getCategoryName(),
                    category.getDescription(),
                    category.getLevel(),
                    childList
            );
        }
    }
}
