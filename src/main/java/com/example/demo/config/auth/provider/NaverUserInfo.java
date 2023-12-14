package com.example.demo.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor // 이것만 하면 디폴트 생성자가 없어서 문제가 발생한다
@NoArgsConstructor // 이렇게해서 디폴트 생성자를 만들어 냈다.
public class NaverUserInfo implements OAuth2UserInfo{ // 그대로 적으면 오류가 나는데 implement Override를 추가해줘야 한다

    private String id;
    private Map<String,Object> attributes;

    @Override
    public String getName() { // String형으로 바꿔 주고
        return (String) attributes.get("nickname");
    }

    @Override
    public String getEmail() { // 어트리뷰트에 email을 받는다
        return (String) attributes.get("email");
    }

    @Override
    public String getProvider() { // 프로바이더
        return "naver";
    }

    @Override
    public String getProviderId() { // 프로바이더의 아이디
        return (String) attributes.get("id");
    }

//    @Override // lombok에서 알아서 받아온다
//    public Map<String, Object> getAttributes() {
//        return null;
//    }
}
