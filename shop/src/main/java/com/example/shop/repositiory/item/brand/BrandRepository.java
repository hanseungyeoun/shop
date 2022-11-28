package com.example.shop.repositiory.item.brand;

import com.example.shop.domain.item.brands.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandToken(String token);
    Page<Brand> findAll(Pageable pageable);

}
