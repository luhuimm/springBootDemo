package com.hui.rabbitmqdemo.rabbitmq.knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.EventInfo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ManualKnowledgeConsumer implements ChannelAwareMessageListener {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
    // 获取消息属性
        MessageProperties messageProperties = message.getMessageProperties();
        // 获取消息分发时的全局唯一标识
        long deliveryTag = messageProperties.getDeliveryTag();
        byte[] msg = message.getBody();
        // 获取消息体
        EventInfo info = null;
        try {
            //解析消息体
            info = objectMapper.readValue(msg, EventInfo.class);
            // 打印日志
            log.info("基本Manual消息模型-消费者-收到消息:{}",info);
            // 手动确认
            channel.basicAck(deliveryTag,true);
        } catch (IOException e) {
            e.printStackTrace();
            // 否则消息一直存在在 队列中，从而导致消息重复消费
            channel.basicReject(deliveryTag,false);
        }
    }
}
