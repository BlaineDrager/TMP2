package com.example.demo.config;


import com.example.demo.config.auth.exceptionHandler.CustomAccessDeniedHandler;
import com.example.demo.config.auth.exceptionHandler.CustomAuthenticationEntryPoint;
import com.example.demo.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.example.demo.config.auth.loginHandler.CustomLoginSuccessHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private HikariDataSource dataSource; // dataSource 를 인자로 넣어야함

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
            //로그인 페이지 연결
            login.loginPage("/login");
            login.successHandler(new CustomLoginSuccessHandler());
            // 로그인 실패시 (인증실패) 로직
            login.failureHandler(new CustomAuthenticationFailureHandler());
        });

//1208 핸들러 설정 시작 컨트롤러는 핸들러의 하위 카테고리에 있는 것이다

        //로그아웃
        http.logout(
            (logout)->{
                logout.permitAll();
                logout.logoutUrl("/logout");
                logout.addLogoutHandler(new CustomLogoutHandler()); // 로그아웃 실패시 핸들러
                logout.logoutSuccessHandler(new CustomLogoutSuccessHandler()); // 로그아웃 성공시 핸들러
            }
        );
        // 예외처리
        http.exceptionHandling(
                ex->{
                    ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증 처리에 문제가 생겼을 때의 예외처리
                    ex.accessDeniedHandler(new CustomAccessDeniedHandler());
                }
        );

        // RememberMe
        http.rememberMe(
                rm->{
                    rm.key("rememberMeKey"); //
                    rm.rememberMeParameter("remember-me");
                    rm.alwaysRemember(false); // 항상 토큰을 주고받을 건지 묻는 용
                    rm.tokenValiditySeconds(3600); //60*60 로그인 유지 시간 // 여기까지가 브라우저에 전달하는 것
                    rm.tokenRepository(tokenRepository()); // db가 읽는다. //
                }
        );
        return http.build();
    }

    // REMEMBER ME 처리하는 BEAN
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//        repo.setCreateTableOnStartup(true);
        repo.setDataSource(dataSource);
        return repo;
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
