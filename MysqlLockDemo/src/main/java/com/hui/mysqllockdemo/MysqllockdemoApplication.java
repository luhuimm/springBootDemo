package com.hui.mysqllockdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.hui.mysqllockdemo.mapper")
public class MysqllockdemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MysqllockdemoApplication.class, args);
    }
    
}
