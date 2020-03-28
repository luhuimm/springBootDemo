package com.hui.rabbitmqdemo.rabbitmq.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicConsumer {
    
    @RabbitListener(queues = "${mq.topic.queue.one}")
    public void consumeDirectMsgOne(@Payload byte[] msg) {
        try {
            String message = new String (msg, "utf-8");
            log.info("队列一上监听到的消息:{}",message);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
    
    @RabbitListener(queues = "${mq.topic.queue.two}")
    public void consumeDirectMsgTwo(@Payload byte[] msg) {
        try {
            String message = new String (msg, "utf-8");
            log.info("队列二上 监听到的消息:{}",message);
        } catch (Exception e) {
            log.info("基本消息模型-消费者-发送异常:{}",e.fillInStackTrace());
        }
    }
}
