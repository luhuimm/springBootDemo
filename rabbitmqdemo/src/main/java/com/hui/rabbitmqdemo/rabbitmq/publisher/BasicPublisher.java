package com.hui.rabbitmqdemo.rabbitmq.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component // 注入到spring 容器
@Slf4j // 日志
public class BasicPublisher {
    
    // 定义序列化和反序列化
    @Autowired
    private ObjectMapper objectMapper;
    
    // 定义操作消息的template
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${mq.queue.name}")
    private String queueName;
    
    @Value("${mq.exchange.name}")
    private String exchangeName;
    
    @Value("${mq.route.key.name}")
    private String routeKeyName;
    
    public void sendMsg(String message)  {
        if (Strings.isNotEmpty(message)) {
            try {
                // 指定序列化方式
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                // 指定交换机名称
                rabbitTemplate.setExchange(exchangeName);
                // 指定路由key
                rabbitTemplate.setRoutingKey(routeKeyName);
                //将字符串转化为待发送的消息
                Message msg = MessageBuilder.withBody(message.getBytes("utf-8")).
                        setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();
                //转换并发送消息
                rabbitTemplate.convertAndSend(msg);
                log.info("基本消息模型-生产者-发送消息:{}",message);
            } catch (UnsupportedEncodingException e) {
               log.error("基本消息模型-生产者-发送消息 异常：{}",e.getMessage());
            }
        }
    }
    
    public void sendObjMsg(Person p) {
        if(p!= null) {
    
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(exchangeName);
            // 指定路由key
            rabbitTemplate.setRoutingKey(routeKeyName);
            
            rabbitTemplate.convertAndSend(p, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    // 获取消息属性
                    MessageProperties messageProperties = message.getMessageProperties();
                    //设置消息持久化模式
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    // 设置消息类型
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME,Person.class);
                    
                    return message;
                }
            });
    
            log.info("基本消息模型-生产者-发送消息:{}",p);
        }
    }
}
