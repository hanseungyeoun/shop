package com.example.shop.controller.admin;

import com.example.shop.common.constant.FormStatus;
import com.example.shop.config.TestSecurityConfig;
import com.example.shop.fixture.BrandInfoFixture;
import com.example.shop.service.BrandService;
import com.example.shop.service.PaginationService;
import com.example.shop.util.FormDataEncoder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.shop.common.constant.FormStatus.*;
import static com.example.shop.dto.item.brand.BrandDto.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Brand 컨트롤러")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(BrandController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class BrandControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean private PaginationService paginationService;
    @MockBean private BrandService brandService;

    /*public BrandControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }*/

    @DisplayName("[view][GET] 새 브랜드 생성 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingBrandAddPage_thenRedirectsToLoginPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/brands/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @WithMockUser(roles = {"ADMIN"}, username = "admin", password = "admin")
    @DisplayName("[view][GET] 새 브랜드 생성 페이지 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingBrandAddPage_thenNewBrandPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/brands/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/brands/addBrand"))
                .andExpect(model().attributeExists("brand"))
                .andExpect(model().attribute("formStatus", CREATE));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 브랜드 등록 - 정상 호출")
    @Test
    void givenNewBrandInfo_whenRequesting_thenSaveNewBrand() throws Exception {
        // Given

        String brandName = "brandName";
        MultipartFile brandImageFile = BrandInfoFixture.createMockMultipartFile("brandImageFile");
        BrandForm brandForm = new BrandForm(brandName, brandImageFile);
        when(brandService.saveBrand(brandForm)).thenReturn(String.valueOf(""));

        // When & Then
        mvc.perform(
            multipart("/admin/brands/add")
                    .file((MockMultipartFile) brandImageFile)
                    .param("brandName", brandName)
                    .with(requestPostProcessor -> {
                        requestPostProcessor.setMethod("POST");
                        return requestPostProcessor;
                    })
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())

                .andExpect(MockMvcResultMatchers.flash().attribute("formStatus", CREATE))
                .andExpect(MockMvcResultMatchers.flash().attribute("redirectUrl", "/admin/brands"))
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"));

        verify(brandService).saveBrand(brandForm);
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 브랜드 등록 - 바인딩 에러")
    @Test
    void givenBindErrorBrandInfo_whenRequesting_thenBindError() throws Exception {
        // Given
        String token = "token";
        String brandName = "brandName";
        MultipartFile brandImageFile = BrandInfoFixture.createMockMultipartFile("brandImageFile");
        BrandForm brandForm = new BrandForm(brandName, brandImageFile);

        when(brandService.saveBrand(brandForm)).thenReturn(token);

        // When & Then
        mvc.perform(
                    multipart("/admin/brands/add")
                            .file((MockMultipartFile) brandImageFile)
                            .param("brandName", "")
                            .with(requestPostProcessor -> {
                                requestPostProcessor.setMethod("POST");
                                return requestPostProcessor;
                            })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(model().attributeExists("brand"))
                .andExpect(model().attribute("formStatus", CREATE))
                .andExpect(view().name("admin/brands/addBrand"));

       verify(brandService, never()).saveBrand(brandForm);
    }

    @DisplayName("[view][GET] 브랜드 리스트 (게시판) 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingBrandView_thenRedirectsToLoginPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/brands"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 브랜드 리스트 (게시판) 페이지")
    @Test
    void givenPagingAndSortingParams_whenRequestingBrandView_thenReturnsBrandView() throws Exception {
        // Given
        String sortName = "createdAt";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        when(brandService.getBrands(pageable)).thenReturn(Page.empty());
        when(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).thenReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/admin/brands")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/brands/index"))
                .andExpect(model().attributeExists("brands"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));

        verify(brandService).getBrands(pageable);
        verify(paginationService).getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 브랜드 수정 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequesting_thenRedirectsToLoginPage() throws Exception {
        // Given
        String brandToken = "token";

        // When & Then
        mvc.perform(get("/admin/brands/" + brandToken + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        verify(brandService, never()).getBrand(eq(brandToken));
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 브랜드 수정 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdateBrandPage() throws Exception {
        // Given
        String brandToken = "token";
        BrandEditForm brand = BrandInfoFixture.createBrandEditFrom();
        when(brandService.getBrand(brandToken)).thenReturn(brand);

        // When & Then
        mvc.perform(get("/admin/brands/" + brandToken + "/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/brands/addBrand"))
                .andExpect(model().attribute("brand", brand))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));

        verify(brandService).getBrand(brandToken);
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedBrandInfo_whenRequesting_thenUpdateNewBrand() throws Exception {
        // Given
        String brandToken = "token";
        BrandEditForm brand = BrandInfoFixture.createBrandEditFrom();
        doNothing().when(brandService).updateBrand(brandToken, brand);

        // When & Then
        mvc.perform(
                multipart("/admin/brands/" + brandToken + "/edit")
                    .file(BrandInfoFixture.createMockMultipartFile("brandImageFile"))
                    .param("brandName", brand.getBrandName())
                    .with(requestPostProcessor -> {
                        requestPostProcessor.setMethod("POST");
                        return requestPostProcessor;
                    })
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 바인딩 오류")
    @Test
    void givenBindErrorUpdatedBrandInfo_whenRequesting_thenBindError() throws Exception {
        // Given
        String brandToken = "token";
        BrandEditForm brand = BrandInfoFixture.createBrandEditFrom();
        doNothing().when(brandService).updateBrand(brandToken, brand);

        // When & Then
        mvc.perform(multipart("/admin/brands/" + brandToken + "/edit")
                        .file(BrandInfoFixture.createMockMultipartFile("brandImageFile"))
                        .param("brandName", "")
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(view().name("admin/brands/addBrand"))
                .andExpect(model().attributeExists("brand"))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));

        verify(brandService, never()).updateBrand(brandToken, brand);
    }

}