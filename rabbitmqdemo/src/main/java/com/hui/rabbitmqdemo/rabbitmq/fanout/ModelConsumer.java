package com.hui.rabbitmqdemo.rabbitmq.fanout;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class ModelConsumer {
    
    // 定义序列化和反序列化
    @Autowired
    private ObjectMapper objectMapper;
    
    
    @RabbitListener(queues = "${mq.fanout.queue.one}")
    public void consumeFanoutMsgOne(@Payload byte[] msg) {
        try {
            EventInfo info = objectMapper.readValue(msg,EventInfo.class);
            log.info("基本消息模型-消费者-监听消费到的消息:{}",info);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
    
    @RabbitListener(queues = "${mq.fanout.queue.two}")
    public void consumeFanoutMsgTwo(@Payload byte[] msg) {
        try {
            EventInfo info = objectMapper.readValue(msg,EventInfo.class);
            log.info("基本消息模型-消费者-监听消费到的消息:{}",info);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
}
