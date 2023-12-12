package com.example.demo.config.auth;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService { // 중요 // 사용자의 정보를 저장하는 곳

    @Autowired(required=true) // DB로 부터 받아오기
    private UserRepository userRepository;

    @Override//DB로부터 ID,Password 받아오기위한 로직
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 넣어진 데이터를 가지고 오는것
        System.out.println("[PrincipalDetailsService] loadUserByUsername() user :" + username);// 어느시점에 하는지 확인용
        Optional<User> userOptional =  userRepository.findById(username);
        if (userOptional.isEmpty())
            return null;

        // Entity 를 Dto로 바꾸는 과정
        UserDto dto = new UserDto();
        dto.setUsername(userOptional.get().getUsername());
        dto.setPassword(userOptional.get().getPassword());
        dto.setRole(userOptional.get().getRole());

        return new PrincipalDetails(dto); // 어덴티케이션에 전달하는 녀석이었다 // 넘기면 알아서 인증작업을 시작한다.
    }
}
