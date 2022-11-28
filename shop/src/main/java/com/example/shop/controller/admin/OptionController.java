package com.example.shop.controller.admin;

import com.example.shop.common.constant.FormStatus;
import com.example.shop.common.respose.CommonResponse;
import com.example.shop.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static com.example.shop.dto.item.option.OptionDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/options")
public class OptionController {

    private final OptionService optionService;

    @ModelAttribute("optionGroups")
    List<OptionGroupResponseDto> optionGroups(){
        List<OptionGroupResponseDto> optionGroups = optionService.getOptionGroups();
        return optionGroups;
    }

    @GetMapping("/add")
    String createOption(Model model) {
        model.addAttribute("option", new OptionForm());
        model.addAttribute("formStatus", FormStatus.UPDATE);
        return "admin/options/optionAdd";
    }

    @PostMapping("/add")
    String postCreateOption(
            @Valid @ModelAttribute("option") OptionForm option,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEditMode", false);
            return "admin/options/optionAdd";
        }

        String token = optionService.createOption(option);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/options");
        return "redirect:/admin/confirm";
    }

    @GetMapping
    String getOptionList(Model model){
        return "admin/options/options";
    }

    @GetMapping("/{optionToken}/edit")
    String editOptionForm(
            @PathVariable String optionToken,
            Model model
    ) {
        OptionForm option = optionService.getOption(optionToken);

        model.addAttribute("option", option);
        model.addAttribute("isEditMode", true);
        return "admin/options/optionAdd";
    }

    @PostMapping("/{optionToken}/edit")
    String postOptionForm(
            @PathVariable String optionToken,
            @Valid @ModelAttribute("option") OptionForm option,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEditMode", true);
            return "admin/options/optionAdd";
        }

        optionService.modifyOption(optionToken, option);
        redirectAttributes.addFlashAttribute("formStatus", FormStatus.CREATE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/options");
        return "redirect:/admin/confirm";
    }

    @ResponseBody
    @DeleteMapping("/{optionToken}/delete")
    CommonResponse deleteOption(@PathVariable String optionToken){
        optionService.deleteOption(optionToken);
        return CommonResponse.success("성공");
    }
}
