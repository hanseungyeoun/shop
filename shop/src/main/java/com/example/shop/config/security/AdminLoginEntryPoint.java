package com.example.shop.config.security;

import com.example.shop.common.exception.ErrorCode;
import com.example.shop.common.respose.CommonResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class AdminLoginEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        /*request.getHeaderNames().asIterator().*/
        String accept = request.getHeader("Accept");
        String contentType = request.getContentType();
        if ((accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) || MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            response.setContentType("application/json");
            response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(CommonResponse.fail(ErrorCode.UNAUTHORIZED).toString());
        } else if ((accept != null && accept.contains(MediaType.TEXT_HTML_VALUE)) || MediaType.TEXT_HTML_VALUE.equals(contentType)) {
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            String errorMessage = "";
            if (authException instanceof BadCredentialsException) {
                errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
            } else if (authException instanceof InternalAuthenticationServiceException) {
                errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
            } else if (authException instanceof UsernameNotFoundException) {
                errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
            } else if (authException instanceof AuthenticationCredentialsNotFoundException) {
                errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
            } else if (authException instanceof InsufficientAuthenticationException) {
                errorMessage = "로그인을 하신 후 사용 하세요.";
            } else {
                errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
            }

            errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            String targetUrl =  "/admin/login?error=true&exception="+errorMessage;
            redirectStrategy.sendRedirect(request, response,targetUrl);
        }
    }
}


