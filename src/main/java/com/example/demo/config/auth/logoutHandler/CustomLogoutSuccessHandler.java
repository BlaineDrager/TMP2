package com.example.demo.config.auth.logoutHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("[CustomLogoutSuccessHandler] onLogoutSuccess");
        // 핸들러는 일종의 처리기로 interseptor나 controller 같은 종류들이 들어가 있다.
        response.sendRedirect("/"); // request.getContextPath() == "/"; 와 같다.
    }
}
