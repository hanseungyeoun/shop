package com.example.shop.config.security;

import com.example.shop.common.exception.ErrorCode;
import com.example.shop.common.respose.CommonResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class AdminAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    protected Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        handle(request, response, exception);
        clearAuthenticationAttributes(request);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        String accept = request.getHeader("Accept");
        String contentType = request.getContentType();
        if ((accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) || MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            writeJsonResponse(request, response, exception);
        } else if ((accept != null && accept.contains(MediaType.TEXT_HTML_VALUE)) || MediaType.TEXT_HTML_VALUE.equals(contentType)) {
            String targetUrl = determineTargetUrl(exception);
            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to "+ targetUrl);
                return;
            }
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }

    }

    protected String determineTargetUrl(AuthenticationException exception) throws IOException {
        String errorMessage = "";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof InsufficientAuthenticationException) {
            errorMessage = "로그인을 하신 후 사용 하세요.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        return "/admin/login?error=true&exception="+errorMessage;
    }

    private void writeJsonResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(CommonResponse.fail(ErrorCode.UNAUTHORIZED).toString());
    }
}
