package com.example.demo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
//    @Column(name = "", length = 1024)
    private String username; // 보통 계정명을 username으로 설정한다.
    private String password;
    private String role;

}
