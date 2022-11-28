package com.example.shop;

import com.example.shop.domain.item.brands.Brand;
import com.example.shop.domain.item.category.Category;
import com.example.shop.domain.item.option.Option;
import com.example.shop.domain.item.option.OptionGroup;
import com.example.shop.repositiory.item.brand.BrandRepository;
import com.example.shop.repositiory.item.category.CategoryRepository;
import com.example.shop.repositiory.item.option.OptionGroupRepository;
import com.example.shop.repositiory.item.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


/*
@Component
*/

@RequiredArgsConstructor
public class TestDataInit {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        Brand brandA = new Brand("아이디스", "066b554f-50ce-4b02-9f7a-398db821df9e.jpg");
        brandRepository.save(brandA);
        brandRepository.save(new Brand("나이키", "066b554f-50ce-4b02-9f7a-398db821df9e.jpg"));

        Category outer = categoryRepository.save(new Category("아우터", "아우터"));
        Category top = categoryRepository.save(new Category("상의", "상의"));

        Category coat = new Category(outer, "코드", "아우터 > 코드");
        categoryRepository.save(coat);

        Category jumper = new Category(outer, "점퍼", "아우터 > 점퍼");
        categoryRepository.save(jumper);

        Category t = new Category(top, "셔츠", "상의 > 셔츠");
        categoryRepository.save(t);

        OptionGroup color = OptionGroup.builder()
                .optionGroupName("색상")
                .optionGroupCode("color")
                .build();

        OptionGroup size = OptionGroup.builder()
                .optionGroupName("사이즈")
                .optionGroupCode("size")
                .build();


        optionGroupRepository.saveAll(List.of(color, size));

        Option os1 = Option.builder()
                .optionGroup(size)
                .optionName("95")
                .optionCode("95")
                .build();

        Option os2 = Option.builder()
                .optionGroup(size)
                .optionName("100")
                .optionCode("100")
                .build();

        Option os3 = Option.builder()
                .optionGroup(size)
                .optionName("105")
                .optionCode("105")
                .build();

        Option os4 = Option.builder()
                .optionGroup(size)
                .optionName("110")
                .optionCode("110")
                .build();

        optionRepository.saveAll(List.of(os1, os2, os3, os4));

        Option oc1 = Option.builder()
                .optionGroup(color)
                .optionName("베이지")
                .optionCode("Beige")
                .build();

        Option oc2 = Option.builder()
                .optionGroup(color)
                .optionName("블랙")
                .optionCode("black")
                .build();

        Option oc3 = Option.builder()
                .optionGroup(color)
                .optionName("브라운")
                .optionCode("brown")

                .build();

        Option oc4 = Option.builder()
                .optionGroup(color)
                .optionName("차콜")
                .optionCode("Charcoal")
                .build();

        optionRepository.saveAll(List.of(oc1, oc2, oc3, oc4));
    }
}
