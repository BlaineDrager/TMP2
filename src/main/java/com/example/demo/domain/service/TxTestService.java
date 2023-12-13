package com.example.demo.domain.service;

import com.example.demo.domain.dto.MemoDto;
import com.example.demo.domain.entity.Memo;
import com.example.demo.domain.mapper.MemoMapper;
import com.example.demo.domain.repository.MemoRepository;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
public class TxTestService { // 테스트용

    @Autowired // 의존성
    private MemoMapper memoMapper;

    @Autowired
    private HikariDataSource dataSource;

    public void txTest() throws SQLException {

        Connection connection =  dataSource.getConnection();

    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "dataSourceTransactionManager")
    public void txMapper() {
        log.info("[TxTestService] txMapper..");

        memoMapper.insert(new MemoDto(1,"aaaa"));
        memoMapper.insert(new MemoDto(1,"bbbb"));
    }

    @Autowired
    private MemoRepository memoRepository;

    @Transactional(rollbackFor = Exception.class,transactionManager = "jpaTransactionManager")
    public void txRepository() throws Exception {
        log.info("[TxTestService] txRepository..");

        memoRepository.save(new Memo(1,"aaaa"));
        throw new Exception();
        //memoRepository.save(new Memo(1,"bbbb"));
    }



}
