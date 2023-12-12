package com.example.demo.config.auth.provider;

import java.util.Map;

public interface OAuth2UserInfo { // 다른 녀석들을 받기위해서 새로운 상위 인터페이스가 필요하다.
    String getName();
    String getEmail();
    String getProvider();
    String getProviderId();

    Map<String, Object> getAttributes();
}