package com.hui.rabbitmqdemo.rabbitmq.knowledge;

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
public class ManualKnowledgePublisher {
    
    // 定义操作消息的template
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    
    @Value("${mq.manual.exchange.name}")
    private String manualExchangeName;
    
    @Value("${mq.manual.key.name}")
    private String manualKeyName;
    
    public void sendManualMsg(EventInfo info) throws JsonProcessingException {
        if (info != null) {
            
            // 指定序列化方式
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 指定交换机名称
            rabbitTemplate.setExchange(manualExchangeName);
            // 指定路由key
            rabbitTemplate.setRoutingKey(manualKeyName);
            
            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
            
            rabbitTemplate.convertAndSend(message);
            
            log.info("基本Manual消息模型-生产者-发送消息:{}",info);
        }
    }
}
