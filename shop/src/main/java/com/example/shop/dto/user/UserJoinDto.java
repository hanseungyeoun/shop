package com.example.shop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public class UserJoinDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserJoinRequest {
        @Size(min = 3, max = 10)
        private String username;
        @Size(min = 3, max = 10)
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserJoinResponse {
        private Long id;
        private String username;

        public static UserJoinResponse fromUser(User user) {
            return new UserJoinResponse(
                    user.getId(),
                    user.getUsername()
            );
        }
    }
}
