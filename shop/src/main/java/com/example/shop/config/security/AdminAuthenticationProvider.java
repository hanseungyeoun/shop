package com.example.shop.config.security;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.dto.user.User;
import com.example.shop.repositiory.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import static com.example.shop.domain.user.RoleType.ROLE_USER;


@Component
@RequiredArgsConstructor
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 검쯩을 위한 구현
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User userDetails = (User)userRepository.findByUsername(username)
                .map(User::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(username);
        }
        if(userDetails.getRole().equals(ROLE_USER)){
            throw new AuthenticationCredentialsNotFoundException(username);
        }

        if(!userDetails.isEnabled()) {
            throw new BadCredentialsException(username);
        }

        return  new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities()
        );
    }

    // 토큰 타입과 일치할 때 인증
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
