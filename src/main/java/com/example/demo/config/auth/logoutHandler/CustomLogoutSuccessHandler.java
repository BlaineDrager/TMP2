package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;


public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")// propeties에 있던 것을 가지고 옴 // 이것을 쓸려면 SecurityConfig의 CUSTOMLOGOUTSUCCESS BEAN를 만들어 줘야함
    private String kakaoClientId;

    private final String REDIRECT_URI = "http://localhost:8080/login";
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        String provider = principalDetails.getUserDto().getProvider();
        System.out.println("[CustomLogoutSuccessHandler] onLogoutSuccess "+ provider);

        // 프로바이더가 있는 경우에 해당하는 경우
        if (provider != null && provider.equals("kakao")) {
            String url = "https://kauth.kakao.com/oauth/logout?client_id="+kakaoClientId+"&logout_redirect_uri="+ REDIRECT_URI;
            response.sendRedirect(url); //
            return;
        } else if (provider != null && provider.equals("naver")) {
            return;
        } else if (provider != null && provider.equals("google")) {
            return;
        }

        System.out.println("[CustomLogoutSuccessHandler] onLogoutSuccess");
        // 핸들러는 일종의 처리기로 interseptor나 controller 같은 종류들이 들어가 있다.
        response.sendRedirect("/"); // request.getContextPath() == "/"; 와 같다.
    }
}
