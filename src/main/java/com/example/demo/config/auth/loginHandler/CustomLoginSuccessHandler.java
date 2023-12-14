package com.example.demo.config.auth.loginHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess()");

        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();
        collection.forEach( (role)->{
            System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess role : " + role);
            String role_str = role.getAuthority();

            try {
                if (role_str.equals("ROLE_USER")){
                    response.sendRedirect("/user");
                } else if (role_str.equals("ROLE_MEMBER")) {
                    response.sendRedirect("/member");
                } else if (role_str.equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

