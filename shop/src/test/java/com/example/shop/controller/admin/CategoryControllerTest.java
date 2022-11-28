package com.example.shop.controller.admin;

import com.example.shop.config.TestSecurityConfig;
import com.example.shop.service.CategoryService;
import com.example.shop.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.shop.common.constant.FormStatus.CREATE;
import static com.example.shop.common.constant.FormStatus.UPDATE;
import static com.example.shop.dto.item.category.CategoryDto.CategoryForm;
import static com.example.shop.dto.item.category.CategoryDto.CategoryResponse;
import static com.example.shop.fixture.CategoryFixture.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Category 컨트롤러")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(CategoryController.class)

class CategoryControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean private CategoryService categoryService;

    public CategoryControllerTest(
            @Autowired  MockMvc mvc,
            @Autowired  FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[view][GET] 카테고리 생성 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingCategoryAddPage_thenRedirectsToLoginPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/categories/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 새 카테고리 생성 페이지 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingCategoryAddPage_thenNewCategoryPage() throws Exception {
        // Given
        List<CategoryResponse> primaryCategories = createResponses();
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(get("/admin/categories/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/categories/addCategory"))
                .andExpect(model().attributeExists("category"))
                .andExpect(model().attribute("primaryCategories", primaryCategories))
                .andExpect(model().attribute("formStatus", CREATE));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 카테고리 등록 - 정상 호출")
    @Test
    void givenNewCategoryInfo_whenRequesting_thenSaveNewCategory() throws Exception {
        // Given
        String token = "token";
        String name = "name";
        String desc = "desc";

        CategoryForm category = createCategoryFrom(name, desc);
        List<CategoryResponse> primaryCategories = createResponses();

        when(categoryService.saveCategory(category)).thenReturn(token);
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(post("/admin/categories/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(category)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(MockMvcResultMatchers.flash().attribute("formStatus", CREATE))
                .andExpect(MockMvcResultMatchers.flash().attribute("redirectUrl", "/admin/categories"));

        verify(categoryService).saveCategory(category);
    }

    @Test
    @DisplayName("[view][POST] 새 카테고리 등록 바이딩 에러 - 등록 페이지로 이동")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenBindError_whenRequesting_thenReturnNewCategoryPage() throws Exception {
        // Given
        String token = "token";
        String name = "";
        String desc = "desc";
        CategoryForm category = createCategoryFrom(name, desc);
        List<CategoryResponse> primaryCategories = createResponses();

        when(categoryService.saveCategory(category)).thenReturn(token);
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(post("/admin/categories/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(category)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/categories/addCategory"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("primaryCategories", primaryCategories))
                .andExpect(model().attribute("formStatus", CREATE));

        verify(categoryService, never()).saveCategory(category);
    }


    @DisplayName("[view][GET] 카테고리 리스트 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingCategoryView_thenRedirectsToCategoryPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void giveNothing_whenRequestingBrandView_thenReturnsBrandView() throws Exception {
        // Given
        List<CategoryResponse> primaryCategories = createResponses();
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(
                    get("/admin/categories")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/categories/index"))
                .andExpect(model().attribute("primaryCategories", primaryCategories));

        verify(categoryService).getPrimaryCategories();
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 새 카테고리 생성 페이지 - 정상 호출")
    @Test
    void givenUpdateCategoryForm_whenRequestingUpdateCategoryPage_thenUpdateCategoryPage() throws Exception {
        // Given
        String token = "token";
        CategoryForm category = createCategoryFrom("name", "desc");
        List<CategoryResponse> primaryCategories = createResponses();
        when(categoryService.getCategory(token)).thenReturn(category);
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(get("/admin/categories/"+ token +"/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/categories/addCategory"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("primaryCategories", primaryCategories))
                .andExpect(model().attribute("formStatus", UPDATE));

        verify(categoryService).getPrimaryCategories();
        verify(categoryService).getCategory(token);
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 카테고리 수정 - 정상 호출")
    @Test
    void givenUpdatedCategoryInfo_whenRequesting_thenUpdateCategory() throws Exception {
        // Given
        String token = "token";
        String name = "name";
        String desc = "desc";

        CategoryForm category = createCategoryFrom(name, desc);
        List<CategoryResponse> primaryCategories = createResponses();
        doNothing().when(categoryService).updateCategory(token, category);
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(post("/admin/categories/"+ token +"/edit")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(category)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(MockMvcResultMatchers.flash().attribute("formStatus", UPDATE))
                .andExpect(MockMvcResultMatchers.flash().attribute("redirectUrl", "/admin/categories"));

        verify(categoryService).updateCategory(token, category);
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 카테고리 수정 - 정상 호출")
    @Test
    void givenBindError_whenRequesting_thenUpdateCategoryPage() throws Exception {
        // Given
        String token = "token";
        String name = "";
        String desc = "desc";

        CategoryForm category = createCategoryFrom(name, desc);
        List<CategoryResponse> primaryCategories = createResponses();
        doNothing().when(categoryService).updateCategory(token, category);
        when(categoryService.getPrimaryCategories()).thenReturn(primaryCategories);

        // When & Then
        mvc.perform(post("/admin/categories/"+ token +"/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(category)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("primaryCategories", primaryCategories))
                .andExpect(model().attribute("formStatus", UPDATE))
                .andExpect(view().name("admin/categories/addCategory"));

        verify(categoryService, never()).updateCategory(token, category);
    }
}