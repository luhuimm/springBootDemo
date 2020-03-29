package com.hui.rabbitmqdemo.rabbitmq.dead;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.rabbitmqdemo.entity.DeadInfo;
import com.hui.rabbitmqdemo.entity.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    
    @RabbitListener(queues = "${mq.real.queue.name}",containerFactory = "simpleRabbitListenerContainerFactory")
    public void consumerMsg(@Payload byte[] msg) {
        try {
            DeadInfo info = objectMapper.readValue(msg,DeadInfo.class);
            log.info("消费者收到的消息是:{}",info);
        } catch (Exception e) {
            log.info("消费者收到的消息异常:{}",e.fillInStackTrace());
        }
    }

}
