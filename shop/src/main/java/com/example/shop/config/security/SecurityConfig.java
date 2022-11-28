package com.example.shop.config.security;

import com.example.shop.config.security.filter.JwtTokenFilter;
import com.example.shop.dto.user.User;
import com.example.shop.repositiory.user.UserRepository;
import com.example.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.secret-key}")
    private String secretKey;
    private final AuthenticationProvider adminAuthenticationProvider;
    private final UserRepository userRepository;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(adminAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .rememberMe().disable()
                .authorizeHttpRequests()
                    .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                    .antMatchers("/api/*/items").permitAll()
                    .antMatchers("/api/v1/orders/**").hasRole("USER")
                    .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenFilter(userDetailsService(userRepository), secretKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(error-> error
                        .authenticationEntryPoint(new ApiLoginEntryPoint())
                );

        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .rememberMe()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/admin/login/**").permitAll()
                        .antMatchers("/admin/join").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(
                        login->login.loginPage("/admin/login")
                                .permitAll()
                                .defaultSuccessUrl("/admin")
                                .failureHandler(authenticationFailureHandler) // 로그인 실패 핸들러

                )
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"));
                    logout.logoutSuccessUrl("/admin/login")
                            .invalidateHttpSession(true).deleteCookies("JSESSIONID");

                })
                .authenticationManager(authManager(http))
                /*.exceptionHandling(error-> error
                        .authenticationEntryPoint(new AdminLoginEntryPoint())
                )*/
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userAccountRepository) {
        return username -> userAccountRepository.findByUsername(username)
                .map(User::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/AdminLTE/**");
    }
}
