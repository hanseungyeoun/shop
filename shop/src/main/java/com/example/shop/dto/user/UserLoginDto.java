package com.example.shop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserLoginDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginRequest{
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginResponse{
        private String token;
    }
}
