package com.example.demo.config.auth;

import com.example.demo.config.auth.provider.GoogleUserInfo;
import com.example.demo.config.auth.provider.KakaoUserInfo;
import com.example.demo.config.auth.provider.NaverUserInfo;
import com.example.demo.config.auth.provider.OAuth2UserInfo;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // throws OAuth2AuthenticationException 예외사항 발생시
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest" + userRequest);
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getClientRegistration()" + userRequest.getClientRegistration());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getAccessToken()" + userRequest.getAccessToken());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getAdditionalParameters()" + userRequest.getAdditionalParameters());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getAccessToken().getTokenValue()" + userRequest.getAccessToken().getTokenValue());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getAccessToken().getTokenType().getValue()" + userRequest.getAccessToken().getTokenType().getValue());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest.getAccessToken().getScopes()" + userRequest.getAccessToken().getScopes());

        //Attribute 확인용
        OAuth2User oAuth2User = super.loadUser(userRequest); // oAuth2User OAthus2에 대한 정보를 받음
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2User : " + oAuth2User);
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2User.getAttributes() : " + oAuth2User.getAttributes());

        //OAuth Server Provider 구별
        String provider = userRequest.getClientRegistration().getRegistrationId(); // provider 에 대한 정보를 꺼내오는 작업
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() provider : " + provider);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (provider != null && provider.equals("kakao")){
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes().get("id").toString(), (Map<String, Object>) oAuth2User.getAttributes().get("properties") );
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() kakaoUserInfo : " + kakaoUserInfo);
            oAuth2UserInfo = kakaoUserInfo; // 이걸 프린시퍼 유저 디테일로 바꿔 주기 위해
        }else if (provider != null && provider.equals("naver")) {
            Map<String, Object> resp = (Map<String, Object>)oAuth2User.getAttributes().get("response");
            String id = (String)resp.get("id");
            NaverUserInfo naverUserInfo = new NaverUserInfo(id, resp);
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() naverUserInfo : " + naverUserInfo);
            oAuth2UserInfo = naverUserInfo;
        }else if (provider != null && provider.equals("google")) {
            // 객체 만들기
            String id = (String) oAuth2User.getAttributes().get("sub");
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(id,oAuth2User.getAttributes());
            oAuth2UserInfo = googleUserInfo;
        }
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2UserInfo : " + oAuth2UserInfo);

        // 계정에 대한 유저 ID와 password
        String username = oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId();
        String password = passwordEncoder.encode("1234");// 인코딩 작업 이 필요함으로 위의 PasswordEncoder를 가져옴 // 훗날엔 "1234" 들어가는 자리를 바꿔줘야함

        //PrincipalDetails 생성이전에 하는 DB조회
        Optional<User> optional = userRepository.findById(username); // 동일한 계정을 찾는 과정
        UserDto dto = null;
        if (optional.isEmpty()) {
            // 동일한 계정이 없으면 새로운 계정을 생성하게
            User user = User // UserEntity 객체를 만들었음
                    .builder()
                    .username(username)
                    .password(password)
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build(); // stream 의 문법을 따라한 형식이다
            userRepository.save(user); // 저장
            dto = User.entityToDto(user); // Entity를 dto로 변경
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() "+ oAuth2UserInfo.getProvider() + " 최초 로그인");
        } else { // 내용이 있단 소리
            User user = optional.get(); // 유저정보를 담아 entity에 담아
            dto = User.entityToDto(user); // dto로 변경시켜줌
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() "+ oAuth2UserInfo.getProvider() + " 기존 계정 로그인");
        }


        //PrincipalDetails 생성 작업
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oAuth2UserInfo.getAttributes()); // 기존에 있던 것에서 Attribute를 꺼내옴
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue()); // AccessToken을 가져오는건데 정확히는 Token의 값을 가지고 온것
        principalDetails.setUserDto(dto); // principal에 넣어줌
        return principalDetails; // 어덴티케이션을 한것
    }
}
