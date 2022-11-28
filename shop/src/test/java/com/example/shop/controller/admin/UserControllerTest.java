package com.example.shop.controller.admin;

import com.example.shop.common.exception.BaseException;
import com.example.shop.common.exception.ErrorCode;
import com.example.shop.config.TestSecurityConfig;
import com.example.shop.controller.api.ApiUserController;
import com.example.shop.dto.user.User;
import com.example.shop.dto.user.UserJoinDto;
import com.example.shop.dto.user.UserLoginDto;
import com.example.shop.service.UserService;
import com.example.shop.util.FormDataEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindException;

import static com.example.shop.domain.user.RoleType.ROLE_ADMIN;
import static com.example.shop.domain.user.RoleType.ROLE_USER;
import static com.example.shop.dto.user.UserJoinDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 회원 가입 로그인 ")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FormDataEncoder formDataEncoder;

    @MockBean private UserService userService;

    public UserControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper,
            @Autowired FormDataEncoder formDataEncoder) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[view][GET] 회원 가입 페이지 호출 - 정상 호출")
    @Test
    @WithAnonymousUser
    void givenNothing_whenRequestingJoinView_thenReturnsJoinView() throws Exception{
        String userName = "name";
        String password = "password";

        mockMvc.perform(get("/admin/join"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/user/join"));
    }

    @DisplayName("[view][POST] 어드민 회원가입  - 정상 호출")
    @Test
    @WithAnonymousUser
    void givenNewUserInfo_whenRequesting_thenSavesNewUserInfo() throws Exception {
        // Given
        String userName = "name";
        String password = "password";
        UserJoinRequest joinRequest = new UserJoinRequest(userName, password);
        when(userService.join(userName, password, ROLE_ADMIN)).thenReturn(Mockito.mock(User.class));

        // When & Then
        mockMvc.perform(
                        post("/admin/join")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(joinRequest))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));

        verify(userService).join(userName, password, ROLE_ADMIN);
    }

    @Test
    @DisplayName("[view][POST] 어드민 중복 아이디 회원가입  - 회원 가입 페이지로 이동 한다")
    @WithAnonymousUser
    void givenDuplicatedUserInfo_whenRequesting_thenReturnJoinView() throws Exception {
        // Given
        String userName = "name";
        String password = "password";
        UserJoinRequest joinRequest = new UserJoinRequest(userName, password);
        when(userService.join(userName, password, ROLE_ADMIN)).thenThrow(new BaseException(ErrorCode.DUPLICATED_USER_NAME));

        // When & Then
        mockMvc.perform(
                        post("/admin/join")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(joinRequest))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/join"));

        verify(userService).join(userName, password, ROLE_ADMIN);
    }

    @Test
    @DisplayName("[view][POST] 어드민 아이디 회원가입 validation 오류 - 회원 가입 페이지로 이동 한다")
    @WithAnonymousUser
    void givenBindError_whenRequesting_thenReturnJoinView() throws Exception {
        // Given
        String userName = "1";
        String password = "1";
        UserJoinRequest joinRequest = new UserJoinRequest(userName, password);

        // When & Then
        mockMvc.perform(
                        post("/admin/join")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(joinRequest))
                )
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/join"));

        verify(userService, never()).join(userName, password, ROLE_ADMIN);
    }

    @DisplayName("[view][GET] 로그인 페이지 - 정상 호출")
    @Test
    void givenNothing_whenTryingToLogIn_thenReturnsLogInView() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/user/login"));

    }
}