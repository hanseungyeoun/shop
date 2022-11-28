package com.example.shop.service;

import com.example.shop.config.security.JwtTokenUtils;
import com.example.shop.dto.user.User;
import com.example.shop.common.exception.BaseException;
import com.example.shop.common.exception.ErrorCode;
import com.example.shop.domain.user.RoleType;
import com.example.shop.domain.user.UserAccount;
import com.example.shop.repositiory.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token-expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public User join(String username, String password, RoleType roleType) {
        userRepository.findByUsername(username).ifPresent(userAccount -> {
            throw new BaseException(ErrorCode.DUPLICATED_USER_NAME);
        });

        UserAccount user = UserAccount.builder()
                .username(username)
                .password(encoder.encode(password))
                .role(roleType)
                .build();

        UserAccount save = userRepository.save(user);
        return User.fromEntity(save);
    }

    public String login(String username, String password) {
        User savedUser = userRepository.findByUsername(username).map(User::fromEntity)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOTFOUND));

        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtTokenUtils.generateAccessToken(username, secretKey, expiredTimeMs);
    }
}
