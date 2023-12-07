package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        //CSRF 비활성화 CSRF를 찾아보자
        http.csrf(
                (config)->{config.disable();} // 람다식은 {}를 제외할 수 있다
        );



        //요청 URL별 접근 제한
        http.authorizeHttpRequests(
                authorize->{
                    authorize.requestMatchers("/","/login").permitAll();
                    authorize.requestMatchers("/user").hasRole("USER"); // ROLE_USER
                    authorize.requestMatchers("/member").hasRole("MEMBER"); // ROLE_MEMBER
                    authorize.requestMatchers("/admin").hasRole("ADMIN"); // ROLE_ADMIN
                    authorize.anyRequest().authenticated();
                }
        );
        //로그인 처리
        http.formLogin(login->{
            login.permitAll();
            //로그엔 페이지 연결
            login.loginPage("/login");
        });

        //로그아웃
        http.logout(
                (logout)->{
                    logout.permitAll();
                    logout.logoutUrl("/logout");
                }
        );
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build());//컨텍스트 홀드에 저장 시킴

        userDetailsManager.createUser(User.withUsername("member")
                .password(passwordEncoder.encode("1234"))
                .roles("MEMBER")
                .build());

        userDetailsManager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build());

        return userDetailsManager;
    }

    // BCryptPasswordEncoder Bean 등록 - 패스워드 검증에 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
