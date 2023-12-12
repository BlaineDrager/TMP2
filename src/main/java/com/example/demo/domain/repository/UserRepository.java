package com.example.demo.domain.repository;

import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository// Bean으로 만들기위한 작업으로 사용한다. MVC 패턴에 사용하기 위해서. 레포지토리는 매퍼와 비슷한 위치로
public interface UserRepository extends JpaRepository<User,String> { // <User,String> 엔티티와 프라이머리키를 받아옴

}
