package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.MemoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MemoMapperTest {

    @Autowired
    private MemoMapper memoMapper;

    @Test
    public void t1(){
        MemoDto dto = new MemoDto(2,"memo");
        memoMapper.insert(dto);
        System.out.println(dto);
    }
    @Test
    public void t2(){
       MemoDto dto =  memoMapper.FindByIdXML(1);
       System.out.println(dto);

    }

}