package com.example.shop.controller.admin;

import com.example.shop.common.constant.FormStatus;
import com.example.shop.dto.item.brand.BrandDto.BrandResponse;
import com.example.shop.service.BrandService;
import com.example.shop.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static com.example.shop.dto.item.brand.BrandDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/brands")
@Slf4j
public class BrandController {

    private final BrandService brandService;
    private final PaginationService paginationService;

    @GetMapping("/add")
    public String brandFrom(Model model){
        model.addAttribute("brand", new BrandForm());
        model.addAttribute("formStatus", FormStatus.CREATE);
        return "admin/brands/addBrand";
    }

    @PostMapping("/add")
    public String postNewBrand(
            @Valid @ModelAttribute("brand") BrandForm request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("formStatus", FormStatus.CREATE);
            return "admin/brands/addBrand";
        }

        brandService.saveBrand(request);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/brands");
        return "redirect:/admin/confirm";
    }

    @GetMapping
    public String brands(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<BrandResponse> brands = brandService.getBrands(pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), brands.getTotalPages());

        model.addAttribute("brands", brands);
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "admin/brands/index";
    }

    @GetMapping("/{brandToken}/edit")
    public String updateBrandForm(@PathVariable String brandToken, Model model){
        BrandEditForm brand = brandService.getBrand(brandToken);

        model.addAttribute("brand", brand);
        model.addAttribute("formStatus", FormStatus.UPDATE);
        return "admin/brands/addBrand";
    }

    @PostMapping("/{brandToken}/edit")
    public String updateBrand(
            @PathVariable String brandToken,
            @Valid @ModelAttribute("brand") BrandEditForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("formStatus", FormStatus.UPDATE);
            return "admin/brands/addBrand";
        }

        brandService.updateBrand(brandToken , form);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.UPDATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/brands");

        return "redirect:/admin/confirm";
    }

}
