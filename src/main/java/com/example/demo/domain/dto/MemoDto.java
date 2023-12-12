package com.example.demo.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoDto { // Dto는 사용자로부터 받아오는 것을 보내기 프레젠테이션영역에 주고 받기를 위해 Dto를 사용하는 것이다.
    private int id;
    private String text;
}
