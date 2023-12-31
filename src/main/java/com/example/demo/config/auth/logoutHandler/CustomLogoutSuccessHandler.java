package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;


public class CustomLogoutSuccessHandler implements LogoutSuccessHandler { // 핸들러는 웹요청을 처리해주는 역할을 한다 컨트롤도 일종의 핸들러이다.

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
            String url = "https://nid.naver.com/nidlogin.logout"; // 리다이렉팅이 되지 않아 일반 네이버 로그인 창으로 넘어가게 됀다. // 해결방법을 찾아보자
            response.sendRedirect(url);
            return;
        } else if (provider != null && provider.equals("google")) {
            String url = "http://accounts.google.com/logout"; // 서버와 연결을 완전히 끊는 처리
            response.sendRedirect(url);
            return;
        }

        System.out.println("[CustomLogoutSuccessHandler] onLogoutSuccess");
        // 핸들러는 일종의 처리기로 interseptor나 controller 같은 종류들이 들어가 있다.
        response.sendRedirect("/"); // request.getContextPath() == "/"; 와 같다.
    }
    /* 시작 네이버 로그인후 네이버 개발자센터로 이동 > 애플리케이션 등록 > 애플리케이션 이름과 사용할 API를 선택 로그인에 필요한 권한을 받을 것을 추가 >
    오픈API 서비스 환경을 설정할건데 PC웹 선택후 서비스 URL와 네이버 로그인 Callback URL를 선택후 등록하기를 누르면 클라이언트 ID와 비밀번호가 생성된다
    이후 두가지를 복사해 어플리케이션.프로퍼티스에있는 naver 부분의 상단 두줄에 각각 클라이언트 ID 와 클라이언트 시크릿을 넣어준다
    다시 브라우저로 가서 도큐먼트의
     */

    /* git 추가 순서
    git branch // 브랜치 확인
    // 만약 브랜치를 생성한다면 git branch [브랜치 이름] 을 적어주면 된다.
    git switch HSH // 내 브랜치로 이동
    git add . //
    git commit -m v0.0 //
    git push origin //
    fatal: The current branch HSH has no upstream branch.
    To push the current branch and set the remote as upstream, use

    git push --set-upstream origin HSH

    To have this happen automatically for branches without a tracking
    upstream, see 'push.autoSetupRemote' in 'git help config'.
    // 이런 형식

    git push -u origin HSH // HSH 브랜치에 push를 하는 작업
    */
}
