package com.example.shop.controller.admin;

import com.example.shop.common.exception.ErrorCode;
import com.example.shop.domain.user.RoleType;
import com.example.shop.service.UserService;
import com.example.shop.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.shop.dto.user.UserJoinDto.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("user", new UserJoinRequest());
        return "admin/user/join";
    }

    @PostMapping("/join")
    public String postSignup(
            @Valid @ModelAttribute("user") UserJoinRequest user,
            BindingResult bindingResult
    ) {
         if (bindingResult.hasErrors()) {
            return "admin/user/join";
        }

        try {
            userService.join(user.getUsername(), user.getPassword(), RoleType.ROLE_ADMIN);
        } catch (BaseException e) {
            if(e.getErrorCode() == ErrorCode.DUPLICATED_USER_NAME) {
                bindingResult.addError(new FieldError(
                        "user", "username",
                        user.getUsername(),
                        false,
                        null,
                        null,
                        e.getMessage()
                ));
                return "admin/user/join";
            } else {
                throw e;
            }
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "admin/user/login";
    }
}
