package com.example.shop.controller.api;

import com.example.shop.common.exception.BaseException;
import com.example.shop.common.exception.ErrorCode;
import com.example.shop.config.TestSecurityConfig;
import com.example.shop.controller.admin.BrandController;
import com.example.shop.dto.user.User;
import com.example.shop.dto.user.UserLoginDto.UserLoginRequest;
import com.example.shop.service.UserService;
import com.example.shop.util.FormDataEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.example.shop.domain.user.RoleType.*;
import static com.example.shop.dto.user.UserJoinDto.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ApiUserController.class)
class ApiUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithAnonymousUser
    public void 회원가입() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.join(userName, password, ROLE_USER)).thenReturn(Mockito.mock(User.class));

        mvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 회원가입시_같은_아이디로_회원가입하면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";
        when(userService.join(userName, password, ROLE_USER)).thenThrow(new BaseException(ErrorCode.DUPLICATED_USER_NAME));

        mvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_USER_NAME.getErrorMsg()))
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void 로그인() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenReturn("testToken");

        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 로그인시_회원가입한적이_없다면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";
        when(userService.login(userName, password)).thenThrow(new BaseException(ErrorCode.USER_NOTFOUND));

        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andExpect(status().is(ErrorCode.USER_NOTFOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOTFOUND.getErrorMsg()))
                .andDo(print());

    }

    @Test
    @WithAnonymousUser
    public void 로그인시_비밀번호가_다르면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new BaseException(ErrorCode.INVALID_PASSWORD));
        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }
}