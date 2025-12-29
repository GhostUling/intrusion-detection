package com.ding.spring_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ding.spring_001.mapper")  // 根据你的实际 Mapper 包名修改
public class Spring001Application {
    public static void main(String[] args) {
        SpringApplication.run(Spring001Application.class, args);
    }
}