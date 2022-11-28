package com.example.shop.service;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.common.exception.IllegalStatusException;
import com.example.shop.domain.item.brands.Brand;
import com.example.shop.fixture.BrandInfoFixture;
import com.example.shop.repositiory.item.brand.BrandRepository;
import com.example.shop.service.file.FileStore;
import com.example.shop.service.file.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

import static com.example.shop.dto.item.brand.BrandDto.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @InjectMocks
    BrandService sut;

    @Mock
    BrandRepository brandRepository;

    @Mock
    FileStore fileStore;

    @Mock
    FileUtil fileUtil;

    @DisplayName("브랜드 정보를 입력하면, 브랜드를 생성한다.")
    @Test
    void givenBrandInfo_whenSavingBrand_thenSavesBrand() throws Exception {
        // Given
        String storeFileName = "aa";
        BrandForm form = BrandInfoFixture.createBrandFrom();
        Brand brand = BrandInfoFixture.createBrand();
        when(fileStore.storeFile(form.getBrandImageFile())).thenReturn(storeFileName);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        // When
        sut.saveBrand(form);

        // Then
        verify(brandRepository).save(any(Brand.class));
    }

    @DisplayName("파일 저장 실패하면, 예외를 던진다.")
    @Test
    void givenBrandInfo_whenSavingBrand_thenThrowsException() throws Exception {
        // Given
        BrandForm form = BrandInfoFixture.createBrandFrom();
        Brand brand = BrandInfoFixture.createBrand();
        when(fileStore.storeFile(form.getBrandImageFile())).thenThrow(new IOException());

        // When
        Throwable t = catchThrowable(() -> sut.saveBrand(form));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalStatusException.class)
                .hasMessage("uploadFileName save file");

       /*
        IllegalStatusException exception = assertThrows(IllegalStatusException.class
                    , () -> sut.saveBrand(form), "uploadFileName save file");*/

        // Then
        verify(fileStore).storeFile(form.getBrandImageFile());
    }

    @DisplayName("브랜드 리스트를 요청하면, 브랜드 페이지를 반환한다.")
    @Test
    void givenNoting_whenSearchingArticles_thenReturnsBrandPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        when(brandRepository.findAll(pageable)).thenReturn(Page.empty());

        // When
        Page<BrandResponse> brands = sut.getBrands(pageable);

        // Then
        assertThat(brands).isEmpty();
        verify(brandRepository).findAll(pageable);
    }

    @DisplayName("브래드 Token으로 조회하면, 브랜드가 반환한다.")
    @Test
    void givenBrandToken_whenGetSearchingBrandToken_thenReturnsBrands() {
        // Given
        String brandToken = "token";
        Brand expected = BrandInfoFixture.createBrand("brandName", "imageName");
        when(brandRepository.findByBrandToken(brandToken)).thenReturn(Optional.of(expected));

        // When
        BrandEditForm actual  = sut.getBrand(brandToken);

        // Then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("brandName", expected.getBrandName())
                .hasFieldOrPropertyWithValue("brandImageName", expected.getBrandImageName());

        verify(brandRepository).findByBrandToken(brandToken);
    }

    @DisplayName("브래드 Token 으로 조회하면, 브랜드가 반환한다.")
    @Test
    void givenNonexistentBrandToken_whenGetSearchingBrandToken_thenThrowsException() {
        // Given
        String brandToken = "token";
        Brand expected = BrandInfoFixture.createBrand("brandName", "imageName");
        when(brandRepository.findByBrandToken(brandToken)).thenReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getBrand(brandToken));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("브랜드가 없습니다");

        verify(brandRepository).findByBrandToken(brandToken);
    }

    @DisplayName("브랜드 정보를 입력하면, 브랜드를 수정한다.")
    @Test
    void givenBrandInfo_whenUpdatingBrandInfo_thenUpdatesBrandInfo() throws IOException {
        // Given
        String brandToken = "token";
        String oldBrandName = "oldBrandName";
        String oldImageName = "oldImage.jpg";

        String updateBrandName = "brandName";
        String newImageName = "image.jpg";

        Brand brand = BrandInfoFixture.createBrand(oldBrandName, oldImageName);
        BrandEditForm form = BrandInfoFixture.createBrandEditFrom(updateBrandName, newImageName);
        when(fileUtil.deleteUploadFile(brand.getBrandImageName())).thenReturn(true);
        when(fileStore.storeFile(form.getBrandImageFile())).thenReturn(newImageName);
        when(brandRepository.findByBrandToken(brandToken)).thenReturn(Optional.of(brand));

        // When
        sut.updateBrand(brandToken, form);

        // Then
        assertThat(brand)
                .hasFieldOrPropertyWithValue("brandName", form.getBrandName())
                .hasFieldOrPropertyWithValue("brandImageName", form.getBrandImageName());

        verify(fileStore).storeFile(form.getBrandImageFile());
        verify(brandRepository).findByBrandToken(brandToken);
    }

    @DisplayName("브랜드 수정시 , 브랜드가 없으면 예외를 발생 시킨다.")
    @Test
    void givenBrandInfo_whenUpdatingBrandInfo_thenThrowsException() throws IOException {
        // Given
        String brandToken = "token";
        BrandEditForm form = BrandInfoFixture.createBrandEditFrom();

        when(brandRepository.findByBrandToken(brandToken)).thenReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.updateBrand(brandToken, form));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("브랜드가 없습니다");

        verify(brandRepository).findByBrandToken(brandToken);
    }

    @DisplayName("브랜드 수정시 , 파일 저장 실패시 예외를 발생 시킨다.")
    @Test
    void givenBrandInfo_whenFileSaveFile_thenThrowsException() throws IOException {
        // Given
        String brandToken = "token";
        Brand brand = BrandInfoFixture.createBrand();
        BrandEditForm form = BrandInfoFixture.createBrandEditFrom();

        when(brandRepository.findByBrandToken(brandToken)).thenReturn(Optional.of(brand));
        when(fileStore.storeFile(form.getBrandImageFile())).thenThrow(new IOException());

        // When
        Throwable t = catchThrowable(() -> sut.updateBrand(brandToken, form));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalStatusException.class)
                .hasMessageContaining("uploadFileName");

        verify(brandRepository).findByBrandToken(brandToken);
        verify(fileStore).storeFile(form.getBrandImageFile());
    }
}