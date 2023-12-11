package com.example.demo.config.auth.logoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("[CustomLogoutHandler] logout()");
        HttpSession session = request.getSession(false);// 세션이 없으면 만들어버리기에 만들어줘야함
        if (session != null)
            session.invalidate();
    }
}
