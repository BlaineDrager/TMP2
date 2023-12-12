package com.example.demo.controller;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class UserController {
// 예외처리는 컨트롤러로 넘겨주는게 좋다. @exceptionHandler 를 이용한다
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // 의존성주입 // 유효성 체크를 한 이후 패스워드를 암호화 시켜주기위해 사용

    @GetMapping("/login")
    public void login(){
        log.info("GET /login...");
    }

    @GetMapping("/user")
    public void user(Authentication authentication, Model model){
        log.info("GET /user...Authentication : " + authentication);
        log.info("username : " + authentication.getName());
        log.info("principal : " + authentication.getPrincipal());
        log.info("authorities : " + authentication.getAuthorities());
        log.info("details : " + authentication.getDetails());
        log.info("credentials : " + authentication.getCredentials());

        model.addAttribute( "authentication", authentication);
    }
    @GetMapping("/member")
    public void member(){
        log.info("GET /member");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("GET /member...Authentication" + authentication);
        log.info("username :" + authentication.getName());
        log.info("principal :" + authentication.getPrincipal());
        log.info("authorities :" + authentication.getAuthorities());
        log.info("details :" + authentication.getDetails());
        log.info("credentials :" + authentication.getCredentials());
    }
    @GetMapping("/admin")
    public void admin(){
        log.info("GET /admin");
    }

    @GetMapping("/join")
    public void join(){
        log.info("GET /join");
    }

    @PostMapping("/join")// 동기 요청
    public String join_post(UserDto dto){
        log.info("POST /join...dto " + dto);
        // 파라미터 받기 UserDto dto여기서 하는중
        // 입력값 검증(유효성체크)

        // 서비스 실행
        dto.setPassword(passwordEncoder.encode(dto.getPassword())); // 패스워드를 암호화 하여 dto에 담음
        boolean isJoin = userService.memberJoin(dto); // userService로 전달

        // View로 속성등을 전달
        if (isJoin)
            return "redirect:login?msg=MemberJoin Success!"; // 절대성주소가 아닌 상대성주소로 넣어주는게 좋다.
        else
            return "redirect:join?msg=Join Failed..";
        // +a 예외처리

        // 컨텍스트안에 들어가는 자바인데 MVC패턴으로 사용하느냐 아닌가에 따라서 @Bean을 쓰는지 안쓰는지 알 수 있다
    }
}
