package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class CustomLogoutHandler implements LogoutHandler {

    private RestTemplate restTemplate;
    public CustomLogoutHandler() {// 생성자 만들기
        restTemplate = new RestTemplate();
    }

    // 네이버 client 아이디와 프라이빗 받기
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("[CustomLogoutHandler] logout()");

        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        String provider = principalDetails.getUserDto().getProvider();
        System.out.println("[CustomLogoutHandler] logout() provider : " + provider);

        if (provider != null && provider.equals("kakao")) {
            //AccessToken 추출하기
            String accessToken = principalDetails.getAccessToken();
            //Request URL
            String url = "https://kapi.kakao.com/v1/user/logout"; // 로그아웃 url 잡기
            //Request Header
            HttpHeaders headers = new HttpHeaders(); // 만약 HttpHeaders가 안잡힌다면 위의 import를 보고 안잡혀있다면 수동으로 넣어줘야한다.
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            headers.add("Authorization", "Bearer "+ accessToken);


            //parameter(생략)
            //MultiValueMap<String,String> params = new LinkedMultiValueMap<>(); // 요청 파라미터에 들어가는것을 확인 // Map의 경우 <> 형식으로 사용하고 <> 내부에 key값과 value 값이 필요하다 key의 경우에는 html의 id 처럼 동일한 값을 넣으면 선행된 key 값의 value는 무시되고 후행된 key값의 value가 나타나게 되기에 동일한 key값을 사용하면 안된다.

            //Header + Parameter 단위 생성
            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity(headers); // MultiValueMap<String,String>로 요청 파라메터를 보낼 수 있게 된다

            //RestTemplate 를 통한 요청
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); // url로 요청 // 여기까지가 로그아웃 처리과정
            // String.class 리플렉션 형식은  함수 클래스는 객체를 만들기위한 기술문서
            System.out.println("[CustomLogoutHandler] logout() resp" + resp);
            System.out.println("[CustomLogoutHandler] logout() resp.getBody()" + resp.getBody());

        } else if (provider != null && provider.equals("naver")) {
            //AccessToken 추출하기
            String accessToken = principalDetails.getAccessToken();
            //Request URL
            String url = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&access_token="+accessToken+"&service.provider=NAVER"; // 네이버 개발자 센터" // 로그아웃 url 잡기

            restTemplate.exchange(url,HttpMethod.GET,null,String.class);

//            //Request Header
//            HttpHeaders headers = new HttpHeaders(); // 만약 HttpHeaders가 안잡힌다면 위의 import를 보고 안잡혀있다면 수동으로 넣어줘야한다.
//            headers.add("Content-Type", "application/x-www-form-urlencoded"); // application/x-www-form-urlencoded form요청 방식
//            headers.add("Authorization", "Bearer "+ accessToken);
//
//
//            //parameter
//            MultiValueMap<String,String> params = new LinkedMultiValueMap<>(); // 요청 파라미터에 들어가는것을 확인
//            params.add("client_id",naverClientId);
//            params.add("client_secret",naverClientSecret);
//            params.add("access_token", accessToken);
//            params.add("grant_type","delete");
//            params.add("service_provider","naver");
//
//
//            //Header + Parameter 단위 생성
//            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity(headers,params); // MultiValueMap<String,String>로 요청 파라메터를 보낼 수 있게 된다
//
//            //RestTemplate 를 통한 요청
//            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); // url로 요청 // 여기까지가 로그아웃 처리과정
//            // String.class 리플렉션 형식은  함수 클래스는 객체를 만들기위한 기술문서
//            System.out.println("[CustomLogoutHandler] logout() resp" + resp);
//            System.out.println("[CustomLogoutHandler] logout() resp.getBody()" + resp.getBody());

        } else if (provider != null && provider.equals("google")) { // 로그아웃시 연결 끊기 작업 중 하나.
            // 액세스 토큰 추출하기
            String accessToken = principalDetails.getAccessToken();
            // URL 잡기
            String url = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken; // 토큰을 만료시키는 작업
            // 레스트 템플릿으로 요청
            ResponseEntity<String> resp =  restTemplate.exchange(url,HttpMethod.GET,null,String.class);
            System.out.println("[CustomLogoutHandler] logout() google resp : "+ resp); // 작동 하는지 확인용
        }

        // http세션 초기화 작업
        HttpSession session = request.getSession(false);// 세션이 없으면 만들어버리기에 만들어줘야함
        if (session != null)
            session.invalidate();
    }
    // 구글 클라이언트 ID
    // 238036395543-t7t1u176c0u31bn2l6m4loscfr6kkvs9.apps.googleusercontent.com
    // 시크릿 키
    // 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
}
