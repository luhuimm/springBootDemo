package com.hui.rabbitmqdemo;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }
    
    /**
     * 定义一个名为：oKong 的队列
     * @return
     */
    @Bean
    public Queue okongQueue() {
        return new Queue("ok1ong");
    }
}
