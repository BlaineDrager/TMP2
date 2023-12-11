package com.example.demo.config.auth.exceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { // 권한이 필요한 페이지에 접속할 때
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("[CustomAuthenticationEntryPoint] commence()" + authException);
        response.sendRedirect("/login?error=" + authException.getMessage()); // redirecting 방식
//        // 포워딩 방식 // request가 유지되냐 안되냐에 따라서 위 아래의 쿼리를 작성하면 된다.
//        request.getRequestDispatcher("/login?error="+authException.getMessage()).forward(request,response);
    }
}
