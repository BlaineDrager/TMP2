package com.example.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class TxTestServiceTest {
    @Autowired
    private TxTestService txTestService;

    @Test
    @Transactional(transactionManager = "dataSourceTransactionManager", rollbackFor = Exception.class)
    public void t1(){
        txTestService.txMapper();
    }

    @Test
    @Transactional(transactionManager = "jpaTransactionManager", rollbackFor = Exception.class)
    public  void t2() throws Exception{
        txTestService.txRepository();
    }
}