package com.hui.rabbitmqdemo.rabbitmq.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


@Component
@Slf4j
public class TopicPublisher {
    
    
    @Value("${mq.topic.exchange.name}")
    private String topicExchangeName;
    
    // 定义操作消息的template
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendMsgTopic(String msg, String routeKey) {
        if(!Strings.isBlank(msg)) {
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(topicExchangeName);
            // 设置路由1
            rabbitTemplate.setRoutingKey(routeKey);
            try {
                Message message = MessageBuilder.withBody(msg.getBytes("utf-8")).build();
                rabbitTemplate.convertAndSend(message);
                log.info("基本消息模型-生产者-发送消息:msg={},routeKey={}",msg, routeKey);
            } catch (UnsupportedEncodingException e) {
                log.info("基本消息模型-生产者-发送消息异常:{}",e.getMessage());
            }
        }
    }
    
}
