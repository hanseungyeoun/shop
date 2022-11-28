package com.example.shop.dto.item.brand;

import com.example.shop.domain.item.brands.Brand;
import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BrandDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BrandForm{
        @NotEmpty(message = "브랜드 이름은 필수값입니다")
        private String brandName;

        @NotNull(message = "브랜드 이미지는 필수값입니다")
        private MultipartFile brandImageFile;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandEditForm{

        @NotEmpty(message = "브랜드 이름은 필수값입니다")
        private String brandName;
        private String brandImageName;
        private MultipartFile brandImageFile;

        public boolean hasImage(){
            return brandImageFile != null
                    && StringUtils.hasText(brandImageFile.getOriginalFilename());
        }

        public static BrandEditForm fromEntity(Brand brand) {
            BrandEditForm brandEditForm = new BrandEditForm();
            brandEditForm.brandName = brand.getBrandName();
            brandEditForm.brandImageName = brand.getBrandImageName();
            return brandEditForm;
        }
    }

    @AllArgsConstructor
    @Data
    public static class BrandResponse {
        private Long id;
        private String brandToken;
        private String brandName;
        private String brandImageName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BrandResponse fromEntity(Brand brand) {
            return new BrandResponse(
                    brand.getId(),
                    brand.getBrandToken(),
                    brand.getBrandName(),
                    brand.getBrandImageName(),
                    brand.getCreatedAt(),
                    brand.getUpdatedAt()
            );
        }
    }
}
