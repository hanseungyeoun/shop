package com.example.shop.config;

import com.example.shop.dto.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () ->{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (null == authentication || !authentication.isAuthenticated()) {
                return null;
            }

            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getUsername());
        };





/*        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .orElse("Unknow");*/
    }
}