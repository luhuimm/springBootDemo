package com.hui.rabbitmqdemo.rabbitmq.knowledge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import com.hui.rabbitmqdemo.entity.KnowledgeInfo;
import com.hui.rabbitmqdemo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoKnowledgePublisher {
    
    // 定义操作消息的template
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${mq.auto.autoQueue}")
    private String autoQueue;
    
    @Value("${mq.auto.exchange.name}")
    private String autoExchangeName;
    
    @Value("${mq.auto.key.name}")
    private String autoKeyName;
    
    public void sendAutoMsg(EventInfo info) throws JsonProcessingException {
        if (info != null) {
            
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(autoExchangeName);
            // 指定路由key
            rabbitTemplate.setRoutingKey(autoKeyName);
            
            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
            
            rabbitTemplate.convertAndSend(message);
            
            log.info("基本Auto消息模型-生产者-发送消息:{}",info);
        }
    }
}
