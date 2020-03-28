package com.hui.rabbitmqdemo.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class BasicConsumer {
    
    // 定义序列化和反序列化
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "${mq.queue.name}")
    public void consumeMsg(@Payload byte[] msg) {
        try {
            String message = new String (msg,"utf-8");
            log.info("基本消息模型-消费者-监听消费到的消息:{}",message);
        } catch (UnsupportedEncodingException e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
    
    @RabbitListener(queues = "${mq.queue.name}")
    public void consumeObjMsg(@Payload Person person) {
            log.info("基本消息模型-消费者-监听消费到的消息:{}",person);
    }
}
