package com.example.shop.controller.admin;

import com.example.shop.common.constant.FormStatus;
import com.example.shop.dto.item.category.CategorySearchType;
import com.example.shop.dto.item.category.CategorySearchCondition;
import com.example.shop.repositiory.item.ItemSearchCondition;
import com.example.shop.common.constant.ItemSearchType;
import com.example.shop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.shop.domain.item.Item.*;
import static com.example.shop.dto.item.brand.BrandDto.*;
import static com.example.shop.dto.item.category.CategoryDto.*;
import static com.example.shop.dto.item.category.CategorySearchType.*;
import static com.example.shop.dto.item.ItemDto.*;

@Controller
@RequestMapping("/admin/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final CategoryService categoryService;
    private final BrandService brandService;
    private final OptionService optionService;
    private final ItemService itemService;
    private final PaginationService paginationService;

    @ModelAttribute("primaryCategories")
    public List<CategoryResponse> getPrimaryCategories(){
        return categoryService.getPrimaryCategories();
    }

    @ModelAttribute("brands")
    public List<BrandResponse> getBrands(){
        return  brandService.getAllBrand();
    }

    @ModelAttribute("salesStatusTypes")
    public SalesStatus[] getSalesStatus(){
        return SalesStatus.values();
    }

    @ModelAttribute("displayStatusTypes")
    public DisplayStatus[] getDisplayStatus(){
        return DisplayStatus.values();
    }

    @ModelAttribute("options")
    public Map<String, Set<String>> getOptionInfo(){
        return optionService.getOptionInfo();
    }

    @GetMapping("/add")
    String addIemForm(Model model) {

        model.addAttribute("item", new ItemForm());
        model.addAttribute("formStatus", FormStatus.CREATE);
        return "admin/items/addItem";
    }

    @PostMapping("/add")
    String addItem(
            @Valid @ModelAttribute("item") ItemForm item,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        log.info("item = {}", item);
        if (item.getMainImageFile() == null ||
                !StringUtils.hasText(item.getMainImageFile().getOriginalFilename())) {
            bindingResult.addError(new FieldError("item", "mainImageFile", "메인이미지를 입력하세요."));
        }

        if (item.getOptionGroups() == null || item.getOptionGroups().get("size").isEmpty())
        {
            bindingResult.addError(new FieldError("item", "optionGroups", item.getOptionGroups(), false, null ,null, "사이즈 옵션을 선택하세요."));
        }

        if (item.getOptionGroups() == null || item.getOptionGroups().get("color").isEmpty())
        {
            bindingResult.addError(new FieldError("item", "optionGroups", item.getOptionGroups(), false, null ,null, "색상 옵션을 선택하세요"));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("formStatus", FormStatus.CREATE);

            if(StringUtils.hasText(item.getPrimaryCategoryToken())){
                CategorySearchCondition categorySearchCondition = new CategorySearchCondition(CATEGORY_TOKEN, item.getPrimaryCategoryToken());
                List<CategoryResponse> secondaryCategory = categoryService.getChildCategory(categorySearchCondition);
                model.addAttribute("secondaryCategories", secondaryCategory);
            }

            return "admin/items/addItem";
        }

        String savedItemToken = itemService.saveItem(item);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/items");
        return "redirect:/admin/confirm";
    }

    @GetMapping
    public String items(
            Model model,
            @ModelAttribute("itemSearch") ItemSearchCondition condition,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ItemResponse> items = itemService.searchItems(condition, pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), items.getTotalPages());

        model.addAttribute("items", items);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("date", LocalDateTime.now());
        model.addAttribute("searchTypes", ItemSearchType.values());

        log.info("itemResponses= {}", items.getContent());
        return "admin/items/items";
    }

    @GetMapping("/{itemToken}/edit")
    String editItemForm(@PathVariable String itemToken, Model model) {
        ItemEditForm item = itemService.getItem(itemToken);
        model.addAttribute("item", item);
        model.addAttribute("formStatus", FormStatus.UPDATE);

        if(StringUtils.hasText(item.getPrimaryCategoryToken())){
            CategorySearchCondition categorySearchCondition = new CategorySearchCondition(CategorySearchType.CATEGORY_TOKEN, item.getPrimaryCategoryToken());
            List<CategoryResponse> secondaryCategory = categoryService.getChildCategory(categorySearchCondition);
            model.addAttribute("secondaryCategories", secondaryCategory);
        }

        return "admin/items/addItem";
    }

    @PostMapping("/{itemToken}/edit")
    String editItem(
            @PathVariable String itemToken,
            @Valid @ModelAttribute("item") ItemEditForm item,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        log.info("item = {}", item);

        if (item.getMainImageFile() == null ||
                !StringUtils.hasText(item.getMainImageFile().getOriginalFilename())) {
            bindingResult.addError(new FieldError("item", "mainImageFile", "메인이미지를 입력하세요."));
        }

        if (item.getOptionGroups() == null || item.getOptionGroups().get("size").isEmpty())
        {
            bindingResult.addError(new FieldError("item", "optionGroups", item.getOptionGroups(), false, null ,null, "사이즈 옵션을 선택하세요."));
        }

        if (item.getOptionGroups() == null || item.getOptionGroups().get("color").isEmpty())
        {
            bindingResult.addError(new FieldError("item", "optionGroups", item.getOptionGroups(), false, null ,null, "색상 옵션을 선택하세요"));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("formStatus", FormStatus.UPDATE);


            if(StringUtils.hasText(item.getPrimaryCategoryToken())){
                CategorySearchCondition categorySearchCondition = new CategorySearchCondition(CategorySearchType.CATEGORY_TOKEN, item.getPrimaryCategoryToken());
                List<CategoryResponse> secondaryCategory = categoryService.getChildCategory(categorySearchCondition);
                model.addAttribute("secondaryCategories", secondaryCategory);
            }

            return "admin/items/addItem";
        }

        itemService.updateItem(itemToken, item);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/items");
        return "redirect:/admin/confirm";
    }
}
