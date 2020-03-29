package com.hui.rabbitmqdemo.rabbitmq.dead;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.DeadInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadPublisher {
    
    
    @Value("${mq.basic.route.key}")
    private String basicRouteKey;
    
    @Value("${mq.basic.exchange.name}")
    private String basicExchangeName;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendMsg(DeadInfo info) throws JsonProcessingException {
    
        // 指定序列化方式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置基本交换机
        rabbitTemplate.setExchange(basicExchangeName);
        // 设置基本路由
        rabbitTemplate.setRoutingKey(basicRouteKey);
    
        Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
        rabbitTemplate.convertAndSend(message);
        
        log.info("死信队列实战--发送对象类型的消息入死信队列的内容为：{}",info);
    }
}
