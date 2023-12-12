package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages ="com.example.demo.domain.repository",transactionManagerRef = "jpaTransactionManager")
public class TxConfig { // 트랜잭션컨피그
    @Autowired
    private HikariDataSource dataSource;

    //JPA TransactionManager Settings // JPA 용
    @Bean(name="jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {// (EntityManagerFactory entityManagerFactory 의존성을 받음
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory); //
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

//    // DataSource용 Tx // Mybatis 용
//    @Bean(name="dataSourceTransactionManager")
//    public DataSourceTransactionManager dataSourceTransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }

}