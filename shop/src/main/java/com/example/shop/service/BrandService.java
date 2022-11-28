package com.example.shop.service;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.common.exception.IllegalStatusException;
import com.example.shop.domain.item.brands.Brand;
import com.example.shop.repositiory.item.brand.BrandRepository;
import com.example.shop.service.file.FileStore;
import com.example.shop.service.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.shop.dto.item.brand.BrandDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final FileStore fileStore;
    private final FileUtil fileUtil;
    private final BrandRepository brandRepository;

    @Transactional
    public String saveBrand(BrandForm brandForm) {
        try {
            String uploadFileName = fileStore.storeFile(brandForm.getBrandImageFile());
            Brand saveBrand = brandRepository.save(new Brand(brandForm.getBrandName(), uploadFileName));
            return saveBrand.getBrandToken();
        } catch (IOException e) {
            throw new IllegalStatusException("uploadFileName save file");
        }
    }

    public Page<BrandResponse> getBrands(Pageable pageRequest) {
        return brandRepository.findAll(pageRequest).map(BrandResponse::fromEntity);
    }

    public List<BrandResponse> getAllBrand() {
        return brandRepository.findAll().stream()
                .map(BrandResponse::fromEntity)
                .collect(Collectors.toList());
    }


    public BrandEditForm getBrand(String brandToken) {
        return brandRepository.findByBrandToken(brandToken)
                .map(BrandEditForm::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("브랜드가 없습니다 - brandToken : " + brandToken));
    }

    @Transactional
    public void updateBrand(String brandToken, BrandEditForm brandForm) {
        try {
            Brand brand = brandRepository.findByBrandToken(brandToken)
                    .orElseThrow(() -> new EntityNotFoundException("브랜드가 없습니다 - brandToken : " + brandToken));

            brand.changeBrandName(brandForm.getBrandName());

            if(brandForm.hasImage()) {
                fileUtil.deleteUploadFile(brand.getBrandImageName());
                brand.changeBrandImage(fileStore.storeFile(brandForm.getBrandImageFile()));
            }
        } catch (IOException e) {
            throw new IllegalStatusException("uploadFileName");
        }
    }
}
