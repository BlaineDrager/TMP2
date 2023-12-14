package com.example.demo.config.auth;

import com.example.demo.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User { // 실제 사용자의 정보를 저장 // useradentycation에 // OAuth2User를 통해서 어덴티케이션을 사용할 수 있다
    private UserDto userDto; // 실제 사용자 정보가 담김

    public PrincipalDetails(UserDto dto) {
        this.userDto = dto;
    }

    // OAUTH2 에 관련된 정보들을 담는것
    private String accessToken;
    private Map<String,Object> attributes; // 속성들을 Map형태로 담아두기 위한 것.
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    // 관리자 메뉴로 false로 잡으면 로그인 조차 못한다

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // getAuthorities 는 Role(역할)이다
        Collection<GrantedAuthority> collection = new ArrayList(); // 여러개라면 포이치문을 써야함
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDto.getRole(); // 유저 역할 정보가 시큐리티에 전달
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
