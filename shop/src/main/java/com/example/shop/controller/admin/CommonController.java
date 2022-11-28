package com.example.shop.controller.admin;

import com.example.shop.common.exception.IllegalStatusException;
import com.example.shop.service.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CommonController {

    @GetMapping("/confirm")
    public String confirm(Model model) {
        if (!model.containsAttribute("redirectUrl")) {
            throw new IllegalStatusException("redirectUrl is null");
        }

        return "admin/common/confirm";
    }
}
