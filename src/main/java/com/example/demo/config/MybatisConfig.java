package com.example.demo.config;


import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.demo.domain.mapper")
public class MybatisConfig {

    @Autowired
    ApplicationContext applicationContext; // MVC패턴의 Bean 정보를 추가

    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean(); // 팩토리빈
        sessionFactory.setDataSource(dataSource); //
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mappers/*.xml")); //
        return sessionFactory.getObject();
    }
}
