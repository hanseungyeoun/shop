package com.example.shop.domain.item.brands;


import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.util.TokenGenerator;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand")
public class Brand extends AbstractEntity {
    private static final String BRAND_PREFIX = "brd_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String brandToken;

    private String brandImageName;

    @Builder
    public Brand(String brandName, String brandImage) {
        if (!StringUtils.hasLength(brandName)) throw new InvalidParamException("brand.brandName");
        if (!StringUtils.hasLength(brandImage)) throw new InvalidParamException("brand.images");

        this.brandToken = TokenGenerator.randomCharacterWithPrefix(BRAND_PREFIX);
        this.brandName = brandName;
        this.brandImageName = brandImage;

    }

    public void changeBrandName(String brandName) {
        if (!StringUtils.hasLength(brandName)) throw new InvalidParamException("brand.brandName");

        this.brandName = brandName;
    }

    public void changeBrandImage(String imageFile) {
        if (!StringUtils.hasLength(imageFile)) throw new InvalidParamException("brand.images");

        this.brandImageName = imageFile;
    }
}
