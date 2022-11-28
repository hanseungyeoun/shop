package com.example.shop.fixture;

import com.example.shop.domain.item.brands.Brand;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.example.shop.dto.item.brand.BrandDto.*;

public class BrandInfoFixture {

    public static Brand createBrand(String brandName, String brandImage){
        Brand brand = Brand.builder()
                .brandName(brandName)
                .brandImage(brandImage)
                .build();

        ReflectionTestUtils.setField(brand, "id", 1L);

        return brand;
    }

    public static Brand createBrand(){
        return createBrand("name", "image");
    }

    public static BrandForm createBrandFrom()  {
        String brandName = "brandName";
        MultipartFile brandImageFile = createMockMultipartFile("brandImage");

        return new BrandForm(brandName, brandImageFile);
    }

    public static BrandEditForm createBrandEditFrom(String brandName, String imageFileName)  {
        MultipartFile brandImageFile = createMockMultipartFile("brandImageFile");
        return new BrandEditForm(brandName, imageFileName, brandImageFile);
    }

    public static BrandEditForm createBrandEditFrom() {
        return createBrandEditFrom("brandName", "image.jpg");
    }

    public static MockMultipartFile createMockMultipartFile(String name){
        String imageFileName = "image.jpg";
        ClassPathResource resource = new ClassPathResource("images/brand_image.jpg");
        InputStream inputStream = null;
        MockMultipartFile mockMultipartFile = null;
        try {
            inputStream = resource.getInputStream();
            mockMultipartFile = new MockMultipartFile(name,
                    imageFileName,
                    MediaType.IMAGE_JPEG_VALUE,
                    inputStream);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mockMultipartFile;
    }
}
