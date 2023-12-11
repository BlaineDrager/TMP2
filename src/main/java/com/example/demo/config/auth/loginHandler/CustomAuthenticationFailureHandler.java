package com.example.demo.config.auth.loginHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //인증 실패시의 핸들러
        System.out.println("[CustomAuthenticationFailureHandler] onAuthenticationFailure() exception" + exception);
        response.sendRedirect("/login?error="+ URLEncoder.encode(exception.getMessage(), "UTF-8")); // URLEncoder 인코딩을 해줘야함
    }
}
