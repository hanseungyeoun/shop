package com.example.shop.service;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.domain.item.category.Category;
import com.example.shop.dto.item.category.CategorySearchCondition;
import com.example.shop.fixture.CategoryFixture;
import com.example.shop.repositiory.item.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.shop.dto.item.category.CategoryDto.*;
import static com.example.shop.dto.item.category.CategorySearchType.CATEGORY_NAME;
import static com.example.shop.dto.item.category.CategorySearchType.CATEGORY_TOKEN;
import static com.example.shop.fixture.CategoryFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService sut;

    @Mock
    CategoryRepository categoryRepository;

    @DisplayName("브랜드 정보를 입력하면, 브랜드를 생성한다.")
    @Test
    void givenCategoryInfo_whenSavingCategory_thenSavesCategory() throws Exception {
        // Given
        CategoryForm form = CategoryFixture.createCategoryFrom();
        when(categoryRepository.save(any())).thenReturn(mock(Category.class));

        // When
        sut.saveCategory(form);

        // Then
        verify(categoryRepository).save(any());
    }

    @DisplayName("2차 카테고리리를 입력하면, 2차 카테고리리를 생성한다.")
    @Test
    void givenSecondaryCategoryInfo_whenSavingCategory_thenSavesCategory() throws Exception {
        // Given
        CategoryForm form = CategoryFixture.createCategoryFromWithParent();
        Category category = CategoryFixture.createCategoryWithParent();

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

        when(categoryRepository.findByCategoryToken(form.getPrimaryCategoryToken()))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(mock(Category.class));

        // When
        sut.saveCategory(form);

        // Then
        verify(categoryRepository).findByCategoryToken(form.getPrimaryCategoryToken());
        verify(categoryRepository, times(1)).save(captor.capture());

        Category saveCategory = captor.getValue();
        assertThat(saveCategory.getCategoryName()).isEqualTo(saveCategory.getCategoryName());
    }

    @DisplayName("1차 카테고리 리스트를 반환 한다.")
    @Test
    void givenNothing_whenSearchingPrimaryCategorise_thenReturnsPrimaryCategorise() {
        // Given
        Category expected = createCategory();
        when(categoryRepository.findAllPrimaryCategory()).thenReturn(List.of(expected));

        // When
        List<CategoryResponse> actual = sut.getPrimaryCategories();

        // Then
        assertThat(actual)
                .hasSize(1)
                .first()
                    .hasFieldOrPropertyWithValue("categoryName", expected.getCategoryName())
                    .hasFieldOrPropertyWithValue("description", expected.getDescription())
                    .hasFieldOrPropertyWithValue("level", expected.getLevel());

        verify(categoryRepository).findAllPrimaryCategory();
    }

    @DisplayName("카테고리 Token 으로 조회하면, 해당하는 카테고리를 반환한다.")
    @Test
    void givenCategoryToken_whenSearchingCategories_thenReturnsCategory() {
        // Given
        String token = "token";
        Category expected = createCategory();
        when(categoryRepository.findByCategoryToken(token)).thenReturn(Optional.of(expected));

        // When
        CategoryForm actual = sut.getCategory(token);

        // Then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("categoryName", expected.getCategoryName())
                .hasFieldOrPropertyWithValue("description", expected.getDescription())
                .hasFieldOrPropertyWithValue("level", expected.getLevel());

        verify(categoryRepository).findByCategoryToken(token);
    }

    @DisplayName("카테고리 Token 없을 시 , 예외를 반환한다")
    @Test
    void givenCategoryToken_whenSearchingCategories_thenThrowsException() {
        // Given
        String token = "token";
        Category expected = createCategory();
        when(categoryRepository.findByCategoryToken(token)).thenReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getCategory(token));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("카테고리가 없습니다 - token: " + token);

        verify(categoryRepository).findByCategoryToken(token);
    }

    @DisplayName("카테고리 수정 정보를 입력하면 , 카테고리를 수정한다.")
    @Test
    void givenModifiedCategoryInfo_whenUpdatingCategoryInfo_thenUpdatesCategory() {
        // Given
        String token = "token";
        Category category = createCategory();
        CategoryForm form = createCategoryFrom("new Name", "new Desc");
        when(categoryRepository.findByCategoryToken(token)).thenReturn(Optional.of(category));

        // When
        sut.updateCategory(token, form);

        // Then
        assertThat(category)
                .hasFieldOrPropertyWithValue("categoryName", form.getCategoryName())
                .hasFieldOrPropertyWithValue("description", form.getDescription());

        verify(categoryRepository).findByCategoryToken(token);
    }

    @DisplayName("카테고리 수정 정보를 입력하면 해당 카테고리가 없을 시, 예외를 반환한다..")
    @Test
    void givenModifiedCategoryInfo_whenUpdatingCategoryInfo_thenThrowsException() {
        // Given
        String token = "token";
        CategoryForm form = createCategoryFrom("new Name", "new Desc");
        when(categoryRepository.findByCategoryToken(token)).thenReturn(Optional.empty());


        // When
        Throwable t = catchThrowable(() -> sut.updateCategory(token, form));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("카테고리가 없습니다 - token: " + token);

        verify(categoryRepository).findByCategoryToken(token);
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        String token = "token";
        doNothing().when(categoryRepository).deleteByCategoryToken(token);

        // When
        sut.deleteCategory(token);

        // Then
        verify(categoryRepository).deleteByCategoryToken(token);
    }

    @DisplayName("카테고리 토큰으로 검색하면, 카테고리 리스트를 반환한다.")
    @Test
    void givenSearchByToken_whenSearchingCategory_thenReturnsCategoryList() {
        // Given
        CategorySearchCondition condition = new CategorySearchCondition();
        condition.setSearchType(CATEGORY_TOKEN);
        condition.setSearchValue("token");

        Category expected = createCategory();
        when(categoryRepository.findByParent_CategoryToken(condition.getSearchValue())).thenReturn(List.of(expected));

        //When
        List<CategoryResponse> actual = sut.getChildCategory(condition);

        //then
        assertThat(actual)
                .hasSize(1)
                .first()
                    .hasFieldOrPropertyWithValue("categoryName", expected.getCategoryName())
                    .hasFieldOrPropertyWithValue("description", expected.getDescription());

        verify(categoryRepository).findByParent_CategoryToken(condition.getSearchValue());
    }

    @DisplayName("카테고리명으로 검색하면, 카테고리 리스트를 반환한다.")
    @Test
    void givenSearchByName_whenSearchingCategory_thenReturnsCategoryList() {
        // Given
        CategorySearchCondition condition = new CategorySearchCondition();
        condition.setSearchType(CATEGORY_NAME);
        condition.setSearchValue("name");

        Category expected = createCategory();
        when(categoryRepository.findByParent_CategoryName(condition.getSearchValue())).thenReturn(List.of(expected));

        //When
        List<CategoryResponse> actual = sut.getChildCategory(condition);

        //then
        assertThat(actual)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("categoryName", expected.getCategoryName())
                .hasFieldOrPropertyWithValue("description", expected.getDescription());

        verify(categoryRepository).findByParent_CategoryName(condition.getSearchValue());
    }
}