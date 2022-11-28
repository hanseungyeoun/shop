package com.example.shop.controller.api;

import com.example.shop.common.respose.CommonResponse;
import com.example.shop.domain.user.RoleType;
import com.example.shop.dto.user.User;
import com.example.shop.dto.user.UserJoinDto;
import com.example.shop.dto.user.UserLoginDto;
import com.example.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;

    @PostMapping("/join")
    public CommonResponse join(@Valid @RequestBody UserJoinDto.UserJoinRequest request) {
        User join = userService.join(request.getUsername(),
                request.getPassword(), RoleType.ROLE_USER);
        UserJoinDto.UserJoinResponse userJoinResponse = UserJoinDto.UserJoinResponse.fromUser(join);

        return CommonResponse.success(userJoinResponse);
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestBody UserLoginDto.UserLoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return CommonResponse.success(new UserLoginDto.UserLoginResponse(token));
    }
}
