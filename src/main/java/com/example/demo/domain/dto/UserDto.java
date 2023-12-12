package com.example.demo.domain.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String role;
//    컨트롤러로 이동시킴
}
