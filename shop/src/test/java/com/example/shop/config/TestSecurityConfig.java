package com.example.shop.config;

import com.example.shop.config.security.SecurityConfig;
import com.example.shop.domain.user.RoleType;
import com.example.shop.domain.user.UserAccount;
import com.example.shop.repositiory.user.UserRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private AuthenticationFailureHandler authenticationFailureHandler;

    @BeforeTestMethod
    public void securitySetUp() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(
                UserAccount.builder()
                        .username("id")
                        .password("password")
                        .role(RoleType.ROLE_ADMIN)
                        .build()
        ));
    }
}
