package com.hui.rabbitmqdemo.rabbitmq.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DirectConsumer {
    
    // 定义序列化和反序列化
    @Autowired
    private ObjectMapper objectMapper;
    
    
    @RabbitListener(queues = "${mq.direct.queue.one}")
    public void consumeDirectMsgOne(@Payload byte[] msg) {
        try {
            EventInfo info = objectMapper.readValue(msg,EventInfo.class);
            log.info("基本消息模型-消费者-监听消费到的消息:{}",info);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
    
    @RabbitListener(queues = "${mq.direct.queue.two}")
    public void consumeDirectMsgTwo(@Payload byte[] msg) {
        try {
            EventInfo info = objectMapper.readValue(msg,EventInfo.class);
            log.info("基本消息模型-消费者-监听消费到的消息:{}",info);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
}
