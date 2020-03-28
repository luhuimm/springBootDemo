package com.hui.rabbitmqdemo.rabbitmq.direct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DirectPublisher {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${mq.direct.exchange.name}")
    private String directExchangeName;
    
    @Value("${mq.direct.key.one.name}")
    private String directKeyOneName;
    
    @Value("${mq.direct.key.two.name}")
    private String directKeyTwoName;
    
    // 定义操作消息的template
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendMsgDirectOne(EventInfo eventInfo) {
        if(eventInfo != null) {
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(directExchangeName);
            // 设置路由1
            rabbitTemplate.setRoutingKey(directKeyOneName);
            try {
                Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(eventInfo)).build();
                rabbitTemplate.convertAndSend(message);
                log.info("基本消息模型-生产者-发送消息:{}",eventInfo);
            } catch (JsonProcessingException e) {
                log.info("基本消息模型-生产者-发送消息异常:{}",e.getMessage());
            }
            
        }
    }
    
    public void sendMsgDirectTwo(EventInfo eventInfo) {
        if(eventInfo != null) {
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(directExchangeName);
            // 设置路由1
            rabbitTemplate.setRoutingKey(directKeyTwoName);
            try {
                Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(eventInfo)).build();
                rabbitTemplate.convertAndSend(message);
                log.info("基本消息模型-生产者-发送消息:{}",eventInfo);
            } catch (JsonProcessingException e) {
                log.info("基本消息模型-生产者-发送消息异常:{}",e.getMessage());
            }
        }
    }
}
