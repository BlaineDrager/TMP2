package com.example.demo.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor // 이것만 하면 디폴트 생성자가 없어서 문제가 발생한다
@NoArgsConstructor // 이렇게해서 디폴트 생성자를 만들어 냈다.
public class KakaoUserInfo implements OAuth2UserInfo{
    // 업캐스팅
    // OAuthUserInfo auth = new kakaoUserInfo();

    // 확장된 부분
    private String id;
    private Map<String,Object> attributes; // 확장된 부분은 직접 접근은 힘듬 하지만 아래의 오버라이드를 통해서 접근이 가능하게 만들었다.

    @Override
    public String getName() {
        return (String)attributes.get("nickname");
//        return attributes.get(""); // 꺼내야 할 대상 s닉네임을 꺼내오는 작업
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return this.id;
    }
}
