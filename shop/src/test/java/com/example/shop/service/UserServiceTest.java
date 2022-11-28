package com.example.shop.service;

import com.example.shop.common.exception.BaseException;
import com.example.shop.common.exception.ErrorCode;
import com.example.shop.domain.user.RoleType;
import com.example.shop.domain.user.UserAccount;
import com.example.shop.fixture.TestInfoFixture;
import com.example.shop.fixture.UserFixture;
import com.example.shop.repositiory.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 로그인이_정상동작한다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUsername(fixture.getUserName())).
                thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));

        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(fixture.getUserName(), fixture.getPassword()));
        verify(userRepository).findByUsername(fixture.getUserName());
    }

    @Test
    void 로그인시_유저가_존재하지_않으면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUsername(fixture.getUserName())).thenReturn(Optional.empty());
        BaseException exception = Assertions.assertThrows(BaseException.class
                ,() -> userService.login(fixture.getUserName(), fixture.getPassword()));

        Assertions.assertEquals(ErrorCode.USER_NOTFOUND, exception.getErrorCode());

        verify(userRepository).findByUsername(fixture.getUserName());
    }

    @Test
    void 로그인시_패스워드가_다르면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUsername(fixture.getUserName())).thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), "password1")));
        when(passwordEncoder.matches(fixture.getPassword(), "password1")).thenReturn(false);

        BaseException exception = Assertions.assertThrows(BaseException.class
                , () -> userService.login(fixture.getUserName(), fixture.getPassword()));

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
        verify(userRepository).findByUsername(fixture.getUserName());
    }

    @Test
    void 회원가입이_정상동작한다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUsername(fixture.getUserName())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(fixture.getPassword())).thenReturn("password_encrypt");
        when(userRepository.save(any(UserAccount.class))).thenReturn(UserFixture.get(fixture.getUserName(), "password_encrypt"));

        Assertions.assertDoesNotThrow(() -> userService.join(fixture.getUserName(), fixture.getPassword(), RoleType.ROLE_USER));
    }

    @Test
    void 회원가입시_아이디가_중복되면_다르면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUsername(fixture.getUserName()))
                .thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));

        BaseException exception = Assertions.assertThrows(BaseException.class,
                () -> userService.join(fixture.getUserName(), fixture.getPassword(), RoleType.ROLE_USER));

        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
        verify(userRepository, never()).save(any());
    }

}