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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    // 아키텍칭을 하는 기술들
    // 문제해결능력을 키워야 한다. 스킬업을 하고싶다면 본인이 스스로 찾는 능력이 길러져야한다.

    @Autowired
    private HikariDataSource dataSource; // dataSource 를 인자로 넣어야하기 위해

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
                    authorize.requestMatchers("/join").hasRole("ANONYMOUS"); // ANONYMOUS 비로그인 계정을 뜻 함
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
//                logout.addLogoutHandler(new CustomLogoutHandler()); // 로그아웃 실패시 핸들러
                logout.addLogoutHandler(customLogoutHandler()); // 로그아웃 실패시 핸들러 2
                logout.logoutSuccessHandler(customLogoutSuccessHandler()); // 로그아웃 성공시 핸들러
            }
        );
        //Session

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
                    rm.tokenRepository(tokenRepository()); // db가 읽는다.
                }
        );

        //Oauth2
        http.oauth2Login(
                oauth2->{
                    oauth2.loginPage("/login"); // 템플렛에 있는 로그인으로 잡아줌
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

    //CUSTOMLOGOUTSUCCESS BEAN 빈등록
    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    //CUSTOMLOGOUTSUCCESS BEAN 빈등록
    @Bean
    public CustomLogoutHandler customLogoutHandler(){
        return new CustomLogoutHandler();
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//
//        userDetailsManager.createUser(User.withUsername("user")
//                .password(passwordEncoder.encode("1234"))
//                .roles("USER")
//                .build());//컨텍스트 홀드에 저장 시킴
//
//        userDetailsManager.createUser(User.withUsername("member")
//                .password(passwordEncoder.encode("1234"))
//                .roles("MEMBER")
//                .build());
//
//        userDetailsManager.createUser(User.withUsername("admin")
//                .password(passwordEncoder.encode("1234"))
//                .roles("ADMIN")
//                .build());
//        // 메모리형태를 임시로 만들어 놓은 것으로 나중에는 DB에 따로 넣어줘야한다.
//
//        return userDetailsManager;
//    }

    // BCryptPasswordEncoder Bean 등록 - 패스워드 검증에 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 부모타입의 객체를 자식 타입으로 강제 형변환하여 자식타입이 가지고 있는 매서드나 필드값을 사용하려 한다면 해당 부모객체는 자식의 생성자로 객체화 되어있어야하는 전제조건이 필요하다.


// 인코딩은 암호화 디코딩은 암호화 해제
}
