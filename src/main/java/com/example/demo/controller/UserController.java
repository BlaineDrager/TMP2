package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class UserController {
// 예외처리는 컨트롤러로 넘겨주는게 좋다. @exceptionHandler 를 이용한다
    @GetMapping("/login")
    public void login(){
        log.info("GET /login...");
    }

    @GetMapping("/user")
    public void user(Authentication authentication, Model model){
        log.info("GET /user...Authentication" + authentication);
        log.info("username :" + authentication);
        log.info("principal :" + authentication.getPrincipal());
        log.info("authorities :" + authentication.getAuthorities());
        log.info("details :" + authentication.getDetails());
        log.info("credentials :" + authentication.getCredentials());

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

}
