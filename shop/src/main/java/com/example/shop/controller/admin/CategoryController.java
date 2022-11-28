package com.example.shop.controller.admin;

import com.example.shop.common.constant.FormStatus;
import com.example.shop.common.respose.CommonResponse;
import com.example.shop.dto.item.category.CategoryDto;
import com.example.shop.dto.item.category.CategorySearchCondition;
import com.example.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static com.example.shop.domain.item.category.Category.*;
import static com.example.shop.dto.item.category.CategoryDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @ModelAttribute("categoryLevel")
    Level[] level(){
        return Level.values();
    }

    @ModelAttribute("primaryCategories")
    List<CategoryResponse> primaryCategories(){
        return categoryService.getPrimaryCategories();
    }

    @GetMapping("/add")
    public String categoryForm(Model model) {
        model.addAttribute("category", new CategoryForm());
        model.addAttribute("formStatus", FormStatus.CREATE);
        return "admin/categories/addCategory";
    }

    @PostMapping("/add")
    public String postNewCategory(
            @Valid @ModelAttribute("category") CategoryForm categoryForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("formStatus", FormStatus.CREATE);
            return "admin/categories/addCategory";
        }

        String token = categoryService.saveCategory(categoryForm);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/categories");
        return "redirect:/admin/confirm";
    }

    @GetMapping
    public String getCategories(Model model) {
        return "admin/categories/index";
    }

    @GetMapping("/{categoryToken}/edit")
    String updateCategoryForm(@PathVariable String categoryToken, Model model) {
        CategoryForm category = categoryService.getCategory(categoryToken);

        model.addAttribute("category", category);
        model.addAttribute("formStatus", FormStatus.UPDATE);
        return "admin/categories/addCategory";
    }


    @PostMapping("/{categoryToken}/edit")
    String updateCategory(
            @PathVariable String categoryToken,
            @Valid @ModelAttribute("category") CategoryForm categoryRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            model.addAttribute("formStatus", FormStatus.UPDATE);
            return "admin/categories/addCategory";
        }

        categoryService.updateCategory(categoryToken, categoryRequest);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.UPDATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/categories");
        return "redirect:/admin/confirm";
    }

    @ResponseBody
    @DeleteMapping("/{categoryToken}/delete")
    CommonResponse deleteCategory(@PathVariable String categoryToken){
        categoryService.deleteCategory(categoryToken);
        return CommonResponse.success("성공");
    }

    @ResponseBody
    @GetMapping("/child")
    public CommonResponse child(CategorySearchCondition condition) {
        List<CategoryDto.CategoryResponse> categoryList = categoryList = categoryService.getChildCategory(condition);
        return CommonResponse.success(categoryList);
    }
}
