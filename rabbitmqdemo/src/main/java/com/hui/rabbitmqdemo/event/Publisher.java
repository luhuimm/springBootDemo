package com.hui.rabbitmqdemo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class Publisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    public void sendMsg() {
        LoginEvent loginEvent = new LoginEvent(this, "debug",
                new SimpleDateFormat("yyyyyy-MM-dd HH:mm:ss").format(new Date()), "127.0.0.1");
        
        applicationEventPublisher.publishEvent(loginEvent);
        
        log.info("spring 事情驱动模型- 发送消息：{}",loginEvent);
        
    }
}
