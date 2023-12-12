package com.example.demo.domain.service;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean memberJoin(UserDto dto) { // 처리할 함수로 MVC 패턴이다
        // 사용자의 정보를 받아서 유저 Dto에 넣어줘야함
        // 비지니스 Validation(유효성) Check 대표적인 예) 기존 패스워드와 패스워드 체크를 하는것

        // Dto를 Entity로 바꾸는 작업이 선행 된 후에 아래의.
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        // Db Saved. (DB작업)
        userRepository.save(user); //save JpaRepository에 있는 것으로 UserRepository가 Jpa를 긁어와서(상속) 사용한다
        return  userRepository.existsById(user.getUsername()); // 값이 제대로 들어갔는지 확인하는 것으로 맞으면 true 아니면 false 여기까지 들어갔으면 컨트롤러로
    }
}
