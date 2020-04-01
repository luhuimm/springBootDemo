package com.hui.redissondemo.queue;

import com.hui.redissondemo.entity.DeadDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class RedissonDelayQueueConsumer {
    
    @Autowired
    private RedissonClient redissonClient ;
    
    @Scheduled(cron = "*/1 * * * * ?")
    public void consumerMsg() throws InterruptedException {
    
        final String dealyQueueName = "redissonDelayQueueV3";
        // 定义获取阻塞式队列实例
        RBlockingDeque<DeadDto> rBlockingDeque = redissonClient.getBlockingDeque(dealyQueueName);
        // 从队列中取出消息
        DeadDto msg = rBlockingDeque.take();
        if (msg != null) {
            log.info("Redisson 延迟队列消息模型--消费者--监听消费真正队列中的消息:{}",msg);
            // 这里执行逻辑代码
            
        }
    }
}
