package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // 빌더 팬터를 사용할 수 있다 (디자인 패턴 중 하나) 여러가지 경우의 수의 생성자를 만들 수 있게 해줌
@AllArgsConstructor // 기본 생성자들 이기에 @Builder 를 사용할땐 @NoArgsConstructor 와 함께 세트로 사용된다
@NoArgsConstructor
@Data
public class UserDto {
    private String username;
    private String password;
    private String role;
//    컨트롤러로 이동시킴

    //OAUTH2
    private String provider;
    private String providerId;
}
