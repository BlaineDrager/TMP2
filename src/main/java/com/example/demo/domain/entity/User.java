package com.example.demo.domain.entity;

import com.example.demo.domain.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor //
@NoArgsConstructor // 매개변수가 없는 생성자
@Data
@Builder // 생성패턴
@Entity
@Table(name = "user")
public class User { // 퍼시스턴스영역 Entity 는 영속계층(Mapper)에 머물러있어야 한다. Mapper 이외에서 사용 될 때에는 Dto로 처리한다. // Entity는 DB의 테이블과 1:1 매핑을 한다. 테이블의 구조를 반영하고 테이블 데이터를 저장하고 관리한다.
    @Id                        //    @Column(name = "", length = 1024)
    private String username; // 보통 계정명을 username으로 설정한다.
    private String password;
    private String role;
    //OAUTH2
    private String provider;
    private String providerId;

    public static UserDto entityToDto(User user){
        UserDto dto = UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .build();
        return dto;
    }

}
