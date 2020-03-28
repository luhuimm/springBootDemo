package com.hui.rabbitmqdemo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import sun.applet.AppletListener;

@Component  // 允许加入spring 容器
@Slf4j  // 日志
@EnableAsync  // 允许异步
public class Consumer implements ApplicationListener<LoginEvent> {
    
    @Async
    @Override
    public void onApplicationEvent(LoginEvent event) {
    
        log.info("spring事情驱动模型-接收消息:{}", event);
        // 后续业务代码处理
        
    }
}
