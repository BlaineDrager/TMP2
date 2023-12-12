package com.example.demo.domain.repository;

import com.example.demo.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    private UserRepository memoRepository;

    @Test
    public void t1(){
        memoRepository.save(new User("user1", "1234", "ROLE_USER"));
    }

}