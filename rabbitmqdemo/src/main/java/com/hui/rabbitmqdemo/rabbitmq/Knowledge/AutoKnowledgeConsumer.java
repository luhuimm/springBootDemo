package com.hui.rabbitmqdemo.rabbitmq.knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import com.hui.rabbitmqdemo.entity.KnowledgeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AutoKnowledgeConsumer {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @RabbitListener(queues = "${mq.auto.autoQueue}",containerFactory = "simpleRabbitListenerContainerFactory")
    public void consumerAutoMsg(@Payload byte[] msg) throws IOException {
    
        EventInfo info =  objectMapper.readValue(msg, EventInfo.class);
    
        log.info("基本Auto消息模型-消费者-收到消息:{}",info);
    }
}
