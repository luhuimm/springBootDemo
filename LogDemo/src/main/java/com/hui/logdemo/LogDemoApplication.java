package com.hui.logdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LogDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogDemoApplication.class, args);
    }

}
